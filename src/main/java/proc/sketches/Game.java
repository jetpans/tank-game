package proc.sketches;

import AiPlayer.AiOutput;
import AiPlayer.GameState;
import AiPlayer.PlayerAi;
import org.locationtech.jts.algorithm.LineIntersector;
import org.locationtech.jts.algorithm.RobustLineIntersector;
import org.locationtech.jts.geom.Coordinate;
import processing.core.PApplet;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;

import static java.lang.Thread.sleep;

public class Game extends PApplet {
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
    public static boolean simulationStop =false;
    public static Instant startTime;
    private static int timeLimit=180;
    //1 unit is 1 second
    public static Wall[] walls = {
            //Outer walls
            new Wall(5, 5, dimX - 5, 5),
            new Wall(5, 5, 5, dimY - 5),
            new Wall(dimX - 5, 5, dimX - 5, dimY - 5),
            new Wall(5, dimY - 5, dimX - 5, dimY - 5),
            new Wall(dimX/2,400,dimX/2,800)
    };
    public static Level myLevel=new Level(walls);


    public void settings() {
        /*Thread playerOneThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    playerAi(null,1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (AWTException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        playerOneThread.start();*/
        Thread playerTwoThread = new Thread(() -> {
            try {
                playerAi(Paths.get("AAA"),0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (AWTException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        playerTwoThread.start();

        size(dimX, dimY);
        int[] redColor = {160, 20, 10};
        int[] blueColor = {5, 5, 120};
        tanks.add(new Tank(dimX / 3, dimY / 3, 0, redColor,0));
        tanks.add(new Tank(dimX * 2 / 3, dimY * 2 / 3, -(float) Math.PI, blueColor,1));
    }

    public void draw() {
        if (!simulationStop) {
            if(ChronoUnit.SECONDS.between(startTime,Instant.now())>timeLimit) {
                simulationStop=true;
            }
            background(255, 255, 255);
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
                    simulationStop=true;
                    SwingUtilities.invokeLater(() -> {
                        String finalText = "WINNER: PLAYER";
                        new TextDrawer("GAME OVER").setVisible(true);
                    });
                }
                tank.update(1, myLevel);
                showTank(tank);
            }

            for (Wall w : myLevel.getWalls()) {
                showWall(w);
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
        } else {
            for (Bullet b : bullets) {
                showBullet(b);
            }
            for (Tank tank : tanks) {
                showTank(tank);
            }
            for (Wall w : myLevel.getWalls()) {
                showWall(w);
            }
        }
    }

    public void showTank(Tank t) {
        rectMode(CENTER);
        fill(t.getColor()[0], t.getColor()[1], t.getColor()[2]);

        translate(t.getPosX(), t.getPosY());
        rotate(t.getAngle());

        strokeWeight(0);
        rect(0, 0, Tank.TANK_SIZE, Tank.TANK_SIZE);

        fill(4, 233, 200);
        strokeWeight(10);
        line(0, 0, Tank.BARREL_SIZE, 0);

        rotate(-t.getAngle());
        translate(-t.getPosX(), -t.getPosY());
    }

    public void showBullet(Bullet b) {
        strokeWeight(0);
        fill(0);
        ellipse(b.getPosX(), b.getPosY(), Bullet.SIZE, Bullet.SIZE);

    }

    public void showWall(Wall w) {
        strokeWeight(5);
        line(w.getX1(), w.getY1(), w.getX2(), w.getY2());
    }

    public void keyPressed() {
        if (simulationStop) {
            return;
        }
        if (key == CODED) {
            activeKeys.add(keyCode);

        }
        activeKeys.add(key);
        switch (key) {
            case 'w':
                tanks.get(0).forward();
                break;
            case 'a':
                tanks.get(0).right();
                break;
            case 's':
                tanks.get(0).backward();
                break;
            case 'd':
                tanks.get(0).left();
                break;
            case 'q':
                bullets.add(tanks.get(0).fireBullet());
                break;
            case ENTER:
                bullets.add(tanks.get(1).fireBullet());
                break;
            case CODED:
                switch (keyCode) {
                    case UP:
                        tanks.get(1).forward();
                        break;
                    case LEFT:
                        tanks.get(1).right();
                        break;
                    case DOWN:
                        tanks.get(1).backward();
                        break;
                    case RIGHT:
                        tanks.get(1).left();
                        break;
                }
                break;
        }
    }

    public void keyReleased() {
        if (simulationStop) {
            return;
        }
        activeKeys.remove(key);
        activeKeys.remove(keyCode);
        if (key == CODED) {
            System.out.println("active keys: "+activeKeys);
        }
        switch (key) {
            case 'w':
                if (!activeKeys.contains('s'))
                    tanks.get(0).stop();
                break;
            case 's':
                if (!activeKeys.contains('w'))
                    tanks.get(0).stop();
                break;
            case 'a':
                if (!activeKeys.contains('d'))
                    tanks.get(0).angleStop();
                break;
            case 'd':
                if (!activeKeys.contains('a'))
                    tanks.get(0).angleStop();
                break;
            case CODED:
                switch (keyCode) {
                    case UP:
                        if (!activeKeys.contains(40))
                            tanks.get(1).stop();
                        break;
                    case DOWN:
                        if (!activeKeys.contains(38))
                            tanks.get(1).stop();
                        break;
                    case LEFT:
                        if (!activeKeys.contains(39))
                            tanks.get(1).angleStop();
                        break;
                    case RIGHT:
                        if (!activeKeys.contains(37))
                            tanks.get(1).angleStop();
                        break;
                }
                break;
        }
    }

    public static void main(String... args) {
        startTime=Instant.now();
        LevelTypes.setDimensions(dimX,dimY);
        String wallsChoice = "1";
        String AbsolutePathToPlayer1Brain = null;
        String AbsolutePathToPlayer2Brain = null;
        Path resultsOutputFile = Paths.get("DefaultResultFile");


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
        PApplet.main("proc.sketches.Game");
    }

    static GameState getCurrentGameState(Integer tankId) {
        /*
        TODO: Generate Current game state for input in AI
         */
        return new GameState();
    }
    void playerAi(Path playerBrain,Integer tankId) throws IOException, AWTException, InterruptedException {
        PlayerAi player = PlayerAi.loadFromFile(playerBrain,tankId);
        while (!simulationStop) {
            sleep(1);
            AiOutput action = player.makeDecisionBasedOnGameState(getCurrentGameState(tankId));
            switch (action.getFireDecision()) {
                case "FIRE":
                    newBullets.add(tanks.get(tankId).fireBullet());
                    break;
            }
            switch (action.getLinearDecision()) {
                case "FORWARD":
                    tanks.get(tankId).forward();
                    break;
                case "BACKWARD":
                    tanks.get(tankId).backward();
                    break;
                case "STOP_LINEAR":
                    tanks.get(tankId).stop();
                    break;
            }
            switch (action.getAngularDecision()) {
                case "RIGHT":
                    tanks.get(tankId).right();
                    break;
                case "LEFT":
                    tanks.get(tankId).left();
                    break;
                case "STOP_ANGULAR":
                    tanks.get(tankId).angleStop();
                    break;
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
