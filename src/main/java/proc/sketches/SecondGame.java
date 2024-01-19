package proc.sketches;

import AiPlayer.AiOutput;
import AiPlayer.GameState;
import AiPlayer.PlayerAi;
import HardCodedPlayer.Point;
import org.locationtech.jts.algorithm.LineIntersector;
import org.locationtech.jts.algorithm.RobustLineIntersector;
import org.locationtech.jts.geom.Coordinate;
import processing.core.PApplet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;

import static proc.sketches.Game.*;

public class SecondGame extends Game {
    public static String winner = null;
    public static ArrayList<Bullet> bullets = new ArrayList<>();
    public static ArrayList<Bullet> deadBullets = new ArrayList<>();
    public static ArrayList<Bullet> newBullets = new ArrayList<>();
    public static Instant LastBullet0 = Instant.now();
    public static Instant LastBullet1 = Instant.now();
    public static int bulletsShot1 = 0;
    public static int bulletsShot2 = 0;
    public static String wayOfVictory = "DRAW";

    public static final int dimX = 1000;
    public static final int dimY = 1000;
    public static HashSet<Object> activeKeys = new HashSet<>();
    public static Wall[] walls = {
            //Outer walls
            new Wall(5, 5, dimX - 5, 5),
            new Wall(5, 5, 5, dimY - 5),
            new Wall(dimX - 5, 5, dimX - 5, dimY - 5),
            new Wall(5, dimY - 5, dimX - 5, dimY - 5),
            new Wall(dimX / 2, 400, dimX / 2, 800)
    };


    public static Level myLevel = new Level(walls);


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
                winner = "Player" + (2 - tank.getId());
                if (res.getOwner().getId() == tank.getId()) {
                    wayOfVictory = "SUICIDE";
                } else {
                    wayOfVictory = "MURDER";
                }
            }
            tank.update(1, myLevel);
        }
        ArrayList<Bullet> tempNewBullets = new ArrayList<>(newBullets);
        if (!tempNewBullets.isEmpty()) {
            for (Bullet b : tempNewBullets) {
                if (b == null) {
                    continue;
                }
                int count = (int) bullets.stream().filter(x -> x.getOwner().equals(b.getOwner())).count();

                if (count < 5) {
                    bullets.add(b);
                    if (count < 5 && b.getCurrentLife() < Bullet.MAX_LIFE) {
                        bullets.add(b);
                        if (b.getOwner().getId() == 0) {
                            bulletsShot1++;
                        } else if (b.getOwner().getId() == 1) {
                            bulletsShot2++;
                        }
                    }

                }
            }
            newBullets.clear();
        }

    }

    public void setup() {

    }

    public void settings() {
        size(dimX, dimY);
    }

    public void draw() {
        background(255, 255, 255);
        int decisions = 0;
        if (winner == null) {
            decisions++;

            if (decisions >= timeLimit) {
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
        } else {

            String str = "WINNER: " + winner + "\n";
            str = str + "TOTAL DECISIONS MADE: " + decisions + "\n";
            str = str + "TOTAL BULLETS SHOT BY PLAYER 1: " + bulletsShot1 + "\n";
            str = str + "TOTAL BULLETS SHOT BY PLAYER 2: " + bulletsShot2 + "\n";
            str = str + "WAY OF VICTORY: " + wayOfVictory + "\n";
            System.out.println(str);
            System.exit(0);
        }
        for (Bullet b : bullets) {
            showBullet(b);
        }
        for (Tank tank : tanks) {
            showTank(tank);
        }
        for (Wall w : myLevel.getWalls()) {
            showWall(w);
        }
        updateGame();


    }

    public static Integer timeLimit;
    public static PlayerAi player0;
    public static PlayerAi player1;

    public static void main(String... args) {

        LevelTypes.setDimensions(dimX, dimY);
        String wallsChoice = "1";
        String AbsolutePathToPlayer1Brain = null;
        String AbsolutePathToPlayer2Brain = null;
        Path resultsOutputFile = Paths.get("DefaultResultFile").toAbsolutePath();
        timeLimit = 100000;
        //1000 unit in a real game would be around 5 seconds
        //1M units is around 1.5seconds for a medium NN
        // >>>>!!!!!ESTIMATED NA TEMELJU 2 POKUSAJE, PLS FORGIVE ME...I'M ONLY HUMAN


        //reading args
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--LevelChoice")) {
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
            } else if (args[i].equals("--TimeLimit")) {
                i++;
                try {
                    timeLimit = Integer.parseInt(args[i]);
                } catch (Throwable ignored) {
                }
            }
        }
        //starting the game simulation
        try {
            //initializing level
            walls = LevelTypes.getWallBlueprint(Integer.parseInt(wallsChoice));
            SecondGame.myLevel = new Level(walls);

            //creating tanks
            int[] redColor = {160, 20, 10};
            int[] blueColor = {5, 5, 120};
            tanks.add(new Tank(dimX / 3, dimY / 3, 0, redColor, 0));
            tanks.add(new Tank(dimX * 2 / 3, dimY * 2 / 3, -(float) Math.PI, blueColor, 1));

            //Loading players
            player0 = PlayerAi.loadFromFile(Paths.get(AbsolutePathToPlayer1Brain), 0);
            player1 = PlayerAi.loadFromFile(Paths.get(AbsolutePathToPlayer2Brain), 1);
            PApplet.main("proc.sketches.SecondGame");

        } catch (Throwable e) {
            System.out.println("ERROR OCCURRED: " + e);
            try {
                Files.createDirectories(resultsOutputFile.getParent());

                if (!Files.exists(resultsOutputFile)) {
                    Files.createFile(resultsOutputFile);
                }
                String str = "ERROR OCCURRED:" + e;
                FileOutputStream outputStream = new FileOutputStream(resultsOutputFile.toFile());
                byte[] strToBytes = str.getBytes();
                outputStream.write(strToBytes);

                outputStream.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
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


    public static GameState getCurrentGameState(Integer tankId) {
        Tank myTank = tanks.get(tankId);
        Tank other = tanks.get(1 - tankId);

        GameState state = new GameState();
        //direct sight
        state.setAngleAtEnemy(calculateAngleFromXAxis(myTank.getPosX(), myTank.getPosY(), other.getPosX(), other.getPosY()));
        state.setCanSeeEnemy(aCanSeeB(myTank.getPosX(), myTank.getPosY(), other.getPosX(), other.getPosY()) ? 1D : -1D);
        state.setDistanceToEnemy(calculateDistance(myTank.getPosX(), myTank.getPosY(), other.getPosX(), other.getPosY()));

        //4directions + bonus for front
        Point closest = getClosestPointInDirectionY((double) myTank.getPosX(), (double) myTank.getPosY(), (double) myTank.getAngle());
        state.setDistanceFront(calculateDistance(myTank.getPosX(), myTank.getPosY(), closest.getX(), closest.getY()));
        state.setSeesFront(aCanSeeB(closest.getX(), closest.getY(), other.getPosX(), other.getPosY()) ? 1D : -1D);

        Double wallAngle = (double) 0;
        if (walls[closest.getOrigin()].getX1() == walls[closest.getOrigin()].getX2()) {
            wallAngle = Math.PI;
        }
        state.setAngleSeesObstacle(myTank.getAngle() - wallAngle);
        state.setEnemySeesObstacle(wallAngle - calculateAngleFromXAxis(other.getPosX(), other.getPosY(), closest.getX(), closest.getY()));

        closest = getClosestPointInDirectionY((double) myTank.getPosX(), (double) myTank.getPosY(), (double) myTank.getAngle() + (Math.PI));
        state.setDistanceBack(calculateDistance(myTank.getPosX(), myTank.getPosY(), closest.getX(), closest.getY()));
        state.setSeesBack(aCanSeeB(closest.getX(), closest.getY(), other.getPosX(), other.getPosY()) ? 1D : -1D);

        closest = getClosestPointInDirectionY((double) myTank.getPosX(), (double) myTank.getPosY(), (double) myTank.getAngle() - (Math.PI / 2));
        state.setDistanceLeft(calculateDistance(myTank.getPosX(), myTank.getPosY(), closest.getX(), closest.getY()));
        state.setSeesLeft(aCanSeeB(closest.getX(), closest.getY(), other.getPosX(), other.getPosY()) ? 1D : -1D);

        closest = getClosestPointInDirectionY((double) myTank.getPosX(), (double) myTank.getPosY(), (double) myTank.getAngle() + (Math.PI / 2));
        state.setDistanceRight(calculateDistance(myTank.getPosX(), myTank.getPosY(), closest.getX(), closest.getY()));
        state.setSeesRight(aCanSeeB(closest.getX(), closest.getY(), other.getPosX(), other.getPosY()) ? 1D : -1D);

        Double radius = (double) Tank.TANK_SIZE * 1.75;
        ArrayList<Bullet> tempBullets = new ArrayList<>(bullets);
        for (Bullet bullet : tempBullets) {
            if (calculateDistance(myTank.getPosX(), myTank.getPosY(), bullet.getPosX(), bullet.getPosY()) <= radius) {
                if (calculateDistance(myTank.getPosX(), myTank.getPosY(), bullet.getPosX(), bullet.getPosY()) <= calculateDistance(myTank.getPosX(), myTank.getPosY(), bullet.getLastX(), bullet.getLastY())) {
                    Double tempAngle = myTank.getAngle() < 0 ? myTank.getAngle() + (2 * Math.PI) : myTank.getAngle();
                    Double tempAngleToBullet = calculateAngleFromXAxis(myTank.getPosX(), myTank.getPosY(), bullet.getPosX(), bullet.getPosY());
                    tempAngleToBullet = tempAngleToBullet < 0 ? tempAngleToBullet + (2 * Math.PI) : tempAngleToBullet;

                    if (Math.abs(tempAngle - tempAngleToBullet) <= Math.PI / 4 || Math.abs(tempAngle - tempAngleToBullet) >= 7 * Math.PI / 4) {
                        state.setBulletsFront(state.getBulletsFront() + 1);
                    } else if (Math.abs(((3 * Math.PI / 2 + tempAngle) % (2 * Math.PI)) - tempAngleToBullet) <= Math.PI / 4 || Math.abs(((3 * Math.PI / 2 + tempAngle) % (2 * Math.PI)) - tempAngleToBullet) >= 7 * Math.PI / 4) {
                        state.setBulletsLeft(state.getBulletsLeft() + 1);
                    } else if (Math.abs(((Math.PI / 2 + tempAngle) % (2 * Math.PI)) - tempAngleToBullet) <= Math.PI / 4 || Math.abs(((Math.PI / 2 + tempAngle) % (2 * Math.PI)) - tempAngleToBullet) >= 7 * Math.PI / 4) {
                        state.setBulletsRight(state.getBulletsRight() + 1);
                    } else if (Math.abs(((Math.PI + tempAngle) % (2 * Math.PI)) - tempAngleToBullet) <= Math.PI / 4 || Math.abs(((Math.PI + tempAngle) % (2 * Math.PI)) - tempAngleToBullet) >= 7 * Math.PI / 4) {
                        state.setBulletsBack(state.getBulletsBack() + 1);
                    }

                }
            }
        }


        return state;
    }


}
