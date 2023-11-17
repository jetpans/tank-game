package proc.sketches;

import AiPlayer.AiOutput;
import AiPlayer.PlayerAi;
import org.locationtech.jts.algorithm.LineIntersector;
import org.locationtech.jts.algorithm.RobustLineIntersector;
import org.locationtech.jts.geom.Coordinate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class GameNoVisuals extends Game {
    public static String winner =null;
    public static final int dimX = 1000;
    public static final int dimY = 1000;
    public static ArrayList<Tank> tanks = new ArrayList<>();
    public static ArrayList<Bullet> bullets = new ArrayList<>();
    public static ArrayList<Bullet> deadBullets = new ArrayList<>();
    public static ArrayList<Bullet> newBullets = new ArrayList<>();
    public static Integer hardcodedPlayerId = null;
    public static Instant LastBullet0 = Instant.now();
    public static Instant LastBullet1 = Instant.now();
    public static int bulletsShot1 = 0;
    public static int bulletsShot2 = 0;
    public static String wayOfVictory = "DRAW";

    public static Wall[] walls;
    public static Level myLevel;

    public static void updateGame() {
        for (Bullet b : bullets) {
            if (b.shouldIDie()) {
                deadBullets.add(b);
                continue;

            }
            b.update(1, myLevel);
        }
        for (Bullet i : deadBullets) {
            bullets.remove(i);
        }
        deadBullets.clear();
        for (Tank tank : tanks) {
            Bullet res = tank.collideWithBullets(bullets);
            if (res != null) {
                winner = "Player" + (2-tank.getId());
                if (res.getOwner().getId()==tank.getId()) {
                    wayOfVictory = "SUICIDE";
                } else {
                    wayOfVictory = "MURDER";
                }
            }
        }
        ArrayList<Bullet> tempNewBullets = new ArrayList<>(newBullets);
        if (!tempNewBullets.isEmpty()) {
            for (Bullet b : tempNewBullets) {
                int count= (int) bullets.stream().filter(x -> x.getOwner().equals(b.getOwner())).count();
                boolean canShoot = true;
                if (b.getOwner().getId()==0 && ChronoUnit.SECONDS.between(LastBullet0,Instant.now())<0.2) {
                    canShoot=false;
                } else if(b.getOwner().getId()==1 && ChronoUnit.SECONDS.between(LastBullet1,Instant.now())<0.2) {
                    canShoot=false;
                }
                if (count<5 && canShoot) {
                    bullets.add(b);
                    if (b.getOwner().getId()==0) {
                        LastBullet0 = Instant.now();
                        bulletsShot1++;
                    } else {
                        LastBullet1 = Instant.now();
                        bulletsShot2++;
                    }

                }
            }
            newBullets.clear();
        }

    }


    public static void main(String... args) {

        LevelTypes.setDimensions(dimX,dimY);
        String wallsChoice = "1";
        String AbsolutePathToPlayer1Brain = null;
        String AbsolutePathToPlayer2Brain = null;
        Path resultsOutputFile = Paths.get("DefaultResultFile");
        Integer timeLimit = 100000;
        //1000 unit in a real game would be around 5 seconds
        // >>>>!!!!!ESTIMATED NA TEMELJU 2 POKUSAJE, PLS FORGIVE ME...I'M ONLY HUMAN


        //reading args
        for (int i= 0; i<args.length;i++) {
            if (args[i].equals("--levelChoice")) {
                i++;
                try {
                    Integer.parseInt(args[i]);
                    wallsChoice = args[i];
                } catch (Throwable e) {
                    //LOL NOTHING
                }
            } else if (args[i].equals("--Player1")) {
                i++;
                AbsolutePathToPlayer1Brain = args[i];
            } else if (args[i].equals("--Player2")) {
                i++;
                AbsolutePathToPlayer2Brain = args[i];
            } else if (args[i].equals("--ResultFile")) {
                i++;
                resultsOutputFile = Paths.get(args[i]);
            } else if (args[i].equals("--timeLimit")) {
                i++;
                try {
                    timeLimit = Integer.parseInt(args[i]);
                } catch (Throwable ignored){}
            }
        }
        //starting the game simulation
        try {
            //initializing level
            walls = LevelTypes.getWallBlueprint(Integer.parseInt(wallsChoice));
            GameNoVisuals.myLevel = new Level(walls);

            //creating tanks
            int[] redColor = {160, 20, 10};
            int[] blueColor = {5, 5, 120};
            tanks.add(new Tank(dimX / 3, dimY / 3, 0, redColor, 0));
            tanks.add(new Tank(dimX * 2 / 3, dimY * 2 / 3, -(float) Math.PI, blueColor, 1));

            //Loading players
            PlayerAi player0 = PlayerAi.loadFromFile(Paths.get(AbsolutePathToPlayer1Brain), 0);
            PlayerAi player1 = PlayerAi.loadFromFile(Paths.get(AbsolutePathToPlayer2Brain), 1);

            int decisions=0;

            while (winner == null) {
                decisions++;

                if (decisions>= timeLimit) {
                    winner = "DRAW";
                }
                //get decision from P1
                AiOutput action0 = player0.makeDecisionBasedOnGameState(getCurrentGameState(0));
                //Implement decision
                switch (action0.getFireDecision()) {
                    case "FIRE":
                        newBullets.add(tanks.get(0).fireBullet());
                        break;
                }
                switch (action0.getLinearDecision()) {
                    case "FORWARD":
                        tanks.get(0).forward();
                        break;
                    case "BACKWARD":
                        tanks.get(0).backward();
                        break;
                    case "STOP_LINEAR":
                        tanks.get(0).stop();
                        break;
                }
                switch (action0.getAngularDecision()) {
                    case "RIGHT":
                        tanks.get(0).right();
                        break;
                    case "LEFT":
                        tanks.get(0).left();
                        break;
                    case "STOP_ANGULAR":
                        tanks.get(0).angleStop();
                        break;
                }

                //get decision from P1
                AiOutput action1 = player1.makeDecisionBasedOnGameState(getCurrentGameState(1));
                //Implement decision
                switch (action1.getFireDecision()) {
                    case "FIRE":
                        newBullets.add(tanks.get(1).fireBullet());
                        break;
                }
                switch (action1.getLinearDecision()) {
                    case "FORWARD":
                        tanks.get(1).forward();
                        break;
                    case "BACKWARD":
                        tanks.get(1).backward();
                        break;
                    case "STOP_LINEAR":
                        tanks.get(1).stop();
                        break;
                }
                switch (action1.getAngularDecision()) {
                    case "RIGHT":
                        tanks.get(1).right();
                        break;
                    case "LEFT":
                        tanks.get(1).left();
                        break;
                    case "STOP_ANGULAR":
                        tanks.get(1).angleStop();
                        break;
                }

                //make decision results visible for next round
                updateGame();
            }

            try {
                Files.createDirectories(resultsOutputFile.getParent());

                if (!Files.exists(resultsOutputFile)) {
                    Files.createFile(resultsOutputFile);
                }
                String str = "WINNER: "+winner+"\n";
                str=str + "TOTAL DECISIONS MADE: "+decisions+"\n";
                str=str+"TOTAL BULLETS SHOT BY PLAYER 1: " + bulletsShot1 +"\n";
                str=str+"TOTAL BULLETS SHOT BY PLAYER 2: " + bulletsShot2 +"\n";
                str=str+"WAY OF VICTORY: "+wayOfVictory +"\n";

                FileOutputStream outputStream = new FileOutputStream(resultsOutputFile.toFile());
                byte[] strToBytes = str.getBytes();
                outputStream.write(strToBytes);

                outputStream.close();
                System.out.println(str);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        } catch (Throwable e) {
            System.out.println("ERROR OCCURRED: " + e );
            try {
                Files.createDirectories(resultsOutputFile.getParent());

                if (!Files.exists(resultsOutputFile)) {
                    Files.createFile(resultsOutputFile);
                }
                String str = "ERROR OCCURRED:" + e ;
                FileOutputStream outputStream = new FileOutputStream(resultsOutputFile.toFile());
                byte[] strToBytes = str.getBytes();
                outputStream.write(strToBytes);

                outputStream.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static boolean aCanSeeB(float x1,float y1,float x2,float y2) {
        boolean result= true;
    for (Wall w: walls) {
        result = result && !checkIfLinesIntersect(x1,x2,y1,y2,w.getX1(),w.getX2(),w.getY1(),w.getY2());
    }
    return result;
    }

    public static boolean checkIfLinesIntersect(float x1, float x2, float y1, float y2,
                             float pi1, float pi2, float qi1, float qi2) {
        Coordinate p1 = new Coordinate(x1, y1);
        Coordinate p2 = new Coordinate(x2, y2);

        // Create line segment 2 with endpoints (0,2) and (2,0)
        Coordinate q1 = new Coordinate(pi1, qi1);
        Coordinate q2 = new Coordinate(pi2, qi2);

        // Create LineIntersector
        LineIntersector lineIntersector = new RobustLineIntersector();
        lineIntersector.computeIntersection(p1, p2, q1, q2);

        // Check if the line segments intersect
        if (lineIntersector.hasIntersection()) {
            Coordinate intersection = lineIntersector.getIntersection(0);
            return true;
        } else {
            return false;
        }
    }

    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        // Calculate the squared differences
        double dx = x2 - x1;
        double dy = y2 - y1;

        // Calculate the squared distance
        double squaredDistance = dx * dx + dy * dy;

        // Calculate the distance by taking the square root
        double distance = Math.sqrt(squaredDistance);

        return distance;
    }

    public static double calculateAngleFromXAxis(double x1, double y1, double x2, double y2) {
        // Calculate the angle using arctangent
        double angle = Math.atan2(y2 - y1, x2 - x1);

        return angle;
    }
}
