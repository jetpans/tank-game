package proc.sketches;

import AiPlayer.AiOutput;
import AiPlayer.GameState;
import AiPlayer.PlayerAi;
import org.locationtech.jts.algorithm.LineIntersector;
import org.locationtech.jts.algorithm.RobustLineIntersector;
import org.locationtech.jts.geom.Coordinate;

import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;

public class GameNoVisuals extends Game {
    public static Integer winner =null;
    public static final int dimX = 1000;
    public static final int dimY = 1000;
    public static ArrayList<Tank> tanks = new ArrayList<>();
    public static ArrayList<Bullet> bullets = new ArrayList<>();
    public static ArrayList<Bullet> deadBullets = new ArrayList<>();
    public static ArrayList<Bullet> newBullets = new ArrayList<>();
    public static HashSet<Object> activeKeys = new HashSet<>();
    public static Integer hardcodedPlayerId = null;
    public static Instant LastBullet0 = Instant.now();
    public static Instant LastBullet1 = Instant.now();

    public static Wall[] walls = {
            //Outer walls
            new Wall(5, 5, dimX - 5, 5),
            new Wall(5, 5, 5, dimY - 5),
            new Wall(dimX - 5, 5, dimX - 5, dimY - 5),
            new Wall(5, dimY - 5, dimX - 5, dimY - 5),
            //Obstacle 1 upper right square
            new Wall(dimX/3,dimY/2,0,dimY/2)



    };
    public static Level myLevel = new Level(walls);

    public void updateGame() {
        for (Bullet b : bullets) {
            if (b.shouldIDie()) {
                deadBullets.add(b);
                continue;

            }
            b.update(1, myLevel);
            showBullet(b);
        }
        for (Bullet i : deadBullets) {
            bullets.remove(i);
        }
        deadBullets.clear();
        for (Tank tank : tanks) {
            Bullet res = tank.collideWithBullets(bullets);
            if (res != null) {
                winner = 1-tank.getId();
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
                    } else {
                        LastBullet1 = Instant.now();
                    }

                }
            }
            newBullets.clear();
        }

    }


    public static void main(String... args) {
        int[] redColor = {160, 20, 10};
        int[] blueColor = {5, 5, 120};
        tanks.add(new Tank(dimX / 3, dimY / 3, 0, redColor,0));
        tanks.add(new Tank(dimX * 2 / 3, dimY * 2 / 3, -(float) Math.PI, blueColor,1));
        PlayerAi player0 = PlayerAi.loadFromFile(Paths.get(args[0]),0);
        PlayerAi player1 = PlayerAi.loadFromFile(Paths.get(args[1]),1);
        while (winner ==null) {
            AiOutput action0 = player0.makeDecisionBasedOnGameState(getCurrentGameState(0));
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
            
            AiOutput action1 = player1.makeDecisionBasedOnGameState(getCurrentGameState(1));
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
            
        }
        System.out.println("winner is: "+winner);
    }
    
    static GameState getCurrentGameState(Integer tankId) {
        /*
        TODO: Generate Current game state for input in AI
         */
        return new GameState();
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
