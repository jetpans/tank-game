package proc.sketches;

//<<<<<<< main
import com.google.gson.*;
import processing.core.PApplet;

import java.awt.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;
=======
import AiPlayer.AiOutput;
import AiPlayer.GameState;
import AiPlayer.PlayerAi;
import HardCodedPlayer.Point;
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
//>>>>>>> merge-branch

import static java.lang.Thread.sleep;

public class Game extends PApplet {
//<<<<<<< main
    public static final int dimX = 800;
    public static final int dimY = 800;
    ArrayList<Tank> tanks = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Bullet> deadBullets = new ArrayList<>();
    ArrayList<Bullet> newBullets = new ArrayList<>();
    HashSet<Object> activeKeys = new HashSet<>();

    Wall[] walls = {new Wall(5, 5, dimX - 5, 5),
=======
    public static final int dimX = 1000;
    public static final int dimY = 1000;
    public static ArrayList<Tank> tanks = new ArrayList<>();
    public static ArrayList<Bullet> bullets = new ArrayList<>();
    public static ArrayList<Bullet> deadBullets = new ArrayList<>();
    public static ArrayList<Bullet> newBullets = new ArrayList<>();
    public static HashSet<Object> activeKeys = new HashSet<>();
    public static Instant LastBullet0 = Instant.now();
    public static Instant LastBullet1 = Instant.now();
    public static int totalBullet1=0;
    public static int totalBullet2=0;
    public static boolean simulationStop =false;
    public static Instant startTime;
    private static int timeLimit=180;
    //1 unit is 1 second

    static String AbsolutePathToPlayer1Brain = null;
    static String AbsolutePathToPlayer2Brain = null;
    public static Wall[] walls = {
            //Outer walls
            new Wall(5, 5, dimX - 5, 5),
//>>>>>>> merge-branch
            new Wall(5, 5, 5, dimY - 5),
            new Wall(dimX - 5, 5, dimX - 5, dimY - 5),
            new Wall(5, dimY - 5, dimX - 5, dimY - 5),
            new Wall(dimX/2,400,dimX/2,800)
    };
    public static Level myLevel=new Level(walls);


    public void settings() {
//<<<<<<< main
        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverCommunicationLoop();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (AWTException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        clientThread.start();

=======
        if (AbsolutePathToPlayer2Brain!=null) {
            Thread playerOneThread = new Thread(() -> {
                try {
                    playerAi(Paths.get(AbsolutePathToPlayer2Brain),1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (AWTException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            playerOneThread.start();
        }
        if (AbsolutePathToPlayer1Brain!=null) {
            Thread playerTwoThread = new Thread(() -> {
                try {
                    playerAi(Paths.get(AbsolutePathToPlayer1Brain), 0);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (AWTException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            playerTwoThread.start();
        }
//>>>>>>> merge-branch
        size(dimX, dimY);
        int[] redColor = {160, 20, 10};
        int[] blueColor = {5, 5, 120};
        tanks.add(new Tank(dimX / 3, dimY / 3, 0, redColor,0));
        tanks.add(new Tank(dimX * 2 / 3, dimY * 2 / 3, -(float) Math.PI/2, blueColor,1));
    }

    public void draw() {
//<<<<<<< main
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
                tank.respawn();
                deadBullets.add(res);
=======
        if (!simulationStop) {
            if(ChronoUnit.SECONDS.between(startTime,Instant.now())>timeLimit) {
                simulationStop=true;
                SwingUtilities.invokeLater(() -> {
                    String finalText = "WINNER: DRAW\n";
                    finalText+= "WAY OF VICTORY: TIME LIMIT\n";
                    finalText+="TIME: "+((float)ChronoUnit.MILLIS.between(startTime,Instant.now()))/1000+"s\n";
                    finalText+="TOTAL BULLETS SHOT BY P1: "+totalBullet1+"\n";
                    finalText+="TOTAL BULLETS SHOT BY P2: "+totalBullet2+"\n";
                    new TextDrawer(finalText).setVisible(true);
                });
//>>>>>>> merge-branch
            }
            background(255, 255, 255);
            for (Bullet b : bullets) {
                if (b.shouldIDie()) {
                    deadBullets.add(b);
                    continue;

//<<<<<<< main
        for (Wall w : myLevel.getWalls()) {
            showWall(w);
=======
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
                        String finalText = "WINNER: PLAYER"+(2- tank.getId())+"\n";
                        if (res.getOwner().getId()==tank.getId()) {
                            finalText+= "WAY OF VICTORY: SUICIDE\n";
                        } else {
                            finalText+= "WAY OF VICTORY: MURDER\n";
                        }
                        finalText+="TIME: "+((float)ChronoUnit.MILLIS.between(startTime,Instant.now()))/1000+"s\n";
                        finalText+="TOTAL BULLETS SHOT BY P1: "+totalBullet1+"\n";
                        finalText+="TOTAL BULLETS SHOT BY P2: "+totalBullet2+"\n";
                        new TextDrawer(finalText).setVisible(true);
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
                    if (b == null) {
                        continue;
                    }
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
                            totalBullet1++;
                        } else {
                            LastBullet1 = Instant.now();
                            totalBullet2++;
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
//>>>>>>> merge-branch
        }
        if (newBullets.size() != 0) {
            bullets.addAll(newBullets);
            newBullets.clear();
        }

//<<<<<<< main
    }

=======
//>>>>>>> merge-branch
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
                newBullets.add(tanks.get(0).fireBullet());
                break;
            case ENTER:
                newBullets.add(tanks.get(1).fireBullet());
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

        //reading args
        for (int i= 0; i<args.length;i++) {
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
            } else if (args[i].equals("--TimeLimit")) {
                i++;
                try {
                    timeLimit = Integer.parseInt(args[i]);
                } catch (Throwable ignored){}
            }
        }
        walls = LevelTypes.getWallBlueprint(Integer.parseInt(wallsChoice));
        myLevel = new Level(walls);
        PApplet.main("proc.sketches.Game");
    }

//<<<<<<< main
    void serverCommunicationLoop() throws IOException, AWTException, InterruptedException {
        Socket clientSocket = new Socket("127.0.0.1", 12345);
        System.out.println("Connected to " + clientSocket.getRemoteSocketAddress());

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

        String userInput;

        while (true) {
            String response = in.readLine();
            if (response == null) {
                continue;
            }
            String[] params = response.split(",");
            if (params.length == 2) {
                String action = params[0];
                Integer tankId = Integer.parseInt(params[1]);
                switch (action) {
                    case "FIRE":
                        newBullets.add(tanks.get(tankId).fireBullet());
                        break;
                    case "FORWARD":
                        tanks.get(tankId).forward();
                        break;
                    case "BACKWARD":
                        tanks.get(tankId).backward();
                        break;
                    case "RIGHT":
                        tanks.get(tankId).right();
                        break;
                    case "LEFT":
                        tanks.get(tankId).left();
                        break;
                    case "STOP_LINEAR":
                        tanks.get(tankId).stop();
                        break;
                    case "STOP_ANGULAR":
                        tanks.get(tankId).angleStop();
                        break;
                    case "GET_STATE":
                        System.out.println("I WAS ASKED FOR STATE");
                        out.println(getStateString());
                        break;
                }
                System.out.println(action);
            }

        }
    }

    String getStateString() {
        Map<String, ArrayList<?>> mapa = new HashMap<>();
        mapa.put("tanks", tanks);
        mapa.put("bullets", bullets);
        ArrayList<Wall> allWalls = (ArrayList<Wall>) Arrays.stream(walls).collect(Collectors.toList());
        mapa.put("walls", allWalls);
        // Convert the tanks list to a JSON string

        // Convert the tanks list to a JSON string
        Gson gson = new Gson();

        // Convert the Map to a JSON string
        String json = gson.toJson(mapa);

        return json;

    }
=======
    static GameState getCurrentGameState(Integer tankId) {
        Tank myTank = tanks.get(tankId);
        Tank other = tanks.get(1-tankId);

        GameState state = new GameState();
        //direct sight
        state.setAngleAtEnemy(calculateAngleFromXAxis(myTank.getPosX(),myTank.getPosY(),other.getPosX(),other.getPosY()));
        state.setCanSeeEnemy(aCanSeeB(myTank.getPosX(),myTank.getPosY(),other.getPosX(),other.getPosY()) ? 1D : -1D);
        state.setDistanceToEnemy(calculateDistance(myTank.getPosX(),myTank.getPosY(),other.getPosX(),other.getPosY()));

        //4directions + bonus for front
        Point closest = getClosestPointInDirectionY((double) myTank.getPosX(), (double) myTank.getPosY(), (double) myTank.getAngle());
        state.setDistanceFront(calculateDistance(myTank.getPosX(),myTank.getPosY(),closest.getX(), closest.getY()));
        state.setSeesFront(aCanSeeB(closest.getX(),closest.getY(),other.getPosX(),other.getPosY()) ? 1D : -1D);

        Double wallAngle= (double) 0;
        if (walls[closest.getOrigin()].getX1() == walls[closest.getOrigin()].getX2()) {
            wallAngle=Math.PI;
        }
        state.setAngleSeesObstacle(myTank.getAngle()-wallAngle);
        state.setEnemySeesObstacle(wallAngle-calculateAngleFromXAxis(other.getPosX(),other.getPosY(),closest.getX(), closest.getY()));

        closest = getClosestPointInDirectionY((double) myTank.getPosX(), (double) myTank.getPosY(), (double) myTank.getAngle()+(Math.PI));
        state.setDistanceBack(calculateDistance(myTank.getPosX(),myTank.getPosY(),closest.getX(), closest.getY()));
        state.setSeesBack(aCanSeeB(closest.getX(),closest.getY(),other.getPosX(),other.getPosY()) ? 1D : -1D);

        closest = getClosestPointInDirectionY((double) myTank.getPosX(), (double) myTank.getPosY(), (double) myTank.getAngle()-(Math.PI/2));
        state.setDistanceLeft(calculateDistance(myTank.getPosX(),myTank.getPosY(),closest.getX(), closest.getY()));
        state.setSeesLeft(aCanSeeB(closest.getX(),closest.getY(),other.getPosX(),other.getPosY()) ? 1D : -1D);

        closest = getClosestPointInDirectionY((double) myTank.getPosX(), (double) myTank.getPosY(), (double) myTank.getAngle()+(Math.PI/2));
        state.setDistanceRight(calculateDistance(myTank.getPosX(),myTank.getPosY(),closest.getX(), closest.getY()));
        state.setSeesRight(aCanSeeB(closest.getX(),closest.getY(),other.getPosX(),other.getPosY()) ? 1D : -1D);

        Double radius = (double) Tank.TANK_SIZE*1.75;

        for (Bullet bullet : bullets) {
            if (calculateDistance(myTank.getPosX(),myTank.getPosY(),bullet.getPosX(),bullet.getPosY())<= radius) {
                if(calculateDistance(myTank.getPosX(),myTank.getPosY(),bullet.getPosX(),bullet.getPosY()) <= calculateDistance(myTank.getPosX(),myTank.getPosY(),bullet.getLastX(),bullet.getLastY())) {
                    Double tempAngle= myTank.getAngle()<0 ? myTank.getAngle()+(2*Math.PI) : myTank.getAngle();
                    Double tempAngleToBullet = calculateAngleFromXAxis(myTank.getPosX(),myTank.getPosY(),bullet.getPosX(),bullet.getPosY());
                    tempAngleToBullet = tempAngleToBullet<0 ? tempAngleToBullet+(2*Math.PI) : tempAngleToBullet;

                    if(Math.abs(tempAngle-tempAngleToBullet)<=Math.PI/4 || Math.abs(tempAngle-tempAngleToBullet)>=7*Math.PI/4) {
                        state.setBulletsFront(state.getBulletsFront()+1);
                    } else if(Math.abs(((3*Math.PI/2+tempAngle)%(2*Math.PI))-tempAngleToBullet)<=Math.PI/4 || Math.abs(((3*Math.PI/2+tempAngle)%(2*Math.PI))-tempAngleToBullet)>=7*Math.PI/4) {
                        state.setBulletsLeft(state.getBulletsLeft()+1);
                    } else if(Math.abs(((Math.PI/2+tempAngle)%(2*Math.PI))-tempAngleToBullet)<=Math.PI/4 || Math.abs(((Math.PI/2+tempAngle)%(2*Math.PI))-tempAngleToBullet)>=7*Math.PI/4) {
                        state.setBulletsRight(state.getBulletsRight()+1);
                    } else if(Math.abs(((Math.PI+tempAngle)%(2*Math.PI))-tempAngleToBullet)<=Math.PI/4 || Math.abs(((Math.PI+tempAngle)%(2*Math.PI))-tempAngleToBullet)>=7*Math.PI/4) {
                        state.setBulletsBack(state.getBulletsBack()+1);
                    }

                }
            }
        }


        return state;
    }

    public static Point getDotRemovedForXinDirectionY(Double x,Double y, Integer X,Double angleY) {
        return new Point((int) (x + X * Math.cos(angleY)), (int) (y + X * Math.sin(angleY)), 100);
    }

    public static Point getClosestPointInDirectionY (Double x,Double y,Double angleY) {
        Point outThere = getDotRemovedForXinDirectionY(x,y,dimX*2,angleY);
        Point closest = null;
        for (int i= 0; i<walls.length;i++) {
            Wall w = walls[i];
            if (closest == null) {
                closest = whereLinesIntersect((float)(double) x,outThere.getX(),(float)(double) y,outThere.getY(),w.getX1(),w.getX2(),w.getY1(),w.getY2(),i);
            } else {
                Point temp = whereLinesIntersect((float)(double) x,outThere.getX(),(float)(double) y,outThere.getY(),w.getX1(),w.getX2(),w.getY1(),w.getY2(),i);
                if (temp != null && calculateDistance(x,y,temp.getX(),temp.getY())<calculateDistance(x,y, closest.getX(), closest.getY())) {
                    closest = temp;
                }
            }
        }
        return closest;
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

    public static Point whereLinesIntersect(float x1, float x2, float y1, float y2,
                                                float pi1, float pi2, float qi1, float qi2,int walId) {
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
            Point point = new Point((int) intersection.getX(), (int) intersection.getY(),walId);
            if (point.getX()>x1) {
                point.setX(point.getX()-2);
            } else {
                point.setX(point.getX()+2);
            }
            if (point.getY()>y1) {
                point.setY(point.getY()-2);
            } else {
                point.setY(point.getY()+2);
            }
            return point;
        } else {
            return null;
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

//>>>>>>> merge-branch
}
