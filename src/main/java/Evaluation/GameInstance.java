package Evaluation;

import AiPlayer.AiOutput;
import AiPlayer.GameState;
import AiPlayer.PlayerAi;
import HardCodedPlayer.Point;
import org.locationtech.jts.algorithm.LineIntersector;
import org.locationtech.jts.algorithm.RobustLineIntersector;
import org.locationtech.jts.geom.Coordinate;
import proc.sketches.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;

import static proc.sketches.Game.*;

public class GameInstance {
    public int dimX = 1000;
    public int dimY = 1000;
    public String winner = null;
    public ArrayList<Tank> tanks = new ArrayList<>();
    public Level myLevel;
    public ArrayList<Bullet> bullets = new ArrayList<>();
    public ArrayList<Bullet> deadBullets = new ArrayList<>();
    public ArrayList<Bullet> newBullets = new ArrayList<>();
    public Wall[] walls;
    public Instant LastBullet0 = Instant.now();
    public Instant LastBullet1 = Instant.now();
    public int bulletsShot1 = 0;
    public int bulletsShot2 = 0;
    public String wayOfVictory = "DRAW";

    public void updateGame() {
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


                if (count < 5 && b.getCurrentLife() < Bullet.MAX_LIFE) {
                    bullets.add(b);
                    if (b.getOwner().getId() == 0) {
                        bulletsShot1++;
                    } else if (b.getOwner().getId() == 1) {
                        bulletsShot2++;
                    }
                }
            }
            newBullets.clear();
        }

    }


    public String start(String firstAgent, String secondAgent, Integer level) {
        LevelTypes.setDimensions(dimX, dimY);
        String wallsChoice = "4";
        Integer timeLimit = 1000;
        //1000 unit in a real game would be around 5 seconds
        //1M units is around 1.5seconds for a medium NN
        // >>>>!!!!!ESTIMATED NA TEMELJU 2 POKUSAJE, PLS FORGIVE ME...I'M ONLY HUMAN

        //starting the game simulation
        try {
            //initializing level
            walls = LevelTypes.getWallBlueprint(level);
            myLevel = new Level(walls);

            //creating tanks
            int[] redColor = {160, 20, 10};
            int[] blueColor = {5, 5, 120};
            tanks.add(new Tank(dimX / 3, dimY / 3, 0, redColor, 0));
            tanks.add(new Tank(dimX * 2 / 3, dimY * 2 / 3, -(float) Math.PI, blueColor, 1));

            //Loading players
            PlayerAi player0 = PlayerAi.loadFromString(firstAgent, 0, this);
            PlayerAi player1 = PlayerAi.loadFromString(secondAgent, 1, this);

            int decisions = 0;

            while (winner == null) {
                decisions++;

                if (decisions >= timeLimit) {
                    winner = "DRAW";
                }
                //get decision from P1

                AiOutput action0 = player0.makeDecisionBasedOnGameState(getCurrentGameStateInstance(0));

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
                AiOutput action1 = player1.makeDecisionBasedOnGameState(getCurrentGameStateInstance(1));
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


            String str = "WINNER: " + winner + "\n";
            str = str + "TOTAL DECISIONS MADE: " + decisions + "\n";
            str = str + "TOTAL BULLETS SHOT BY PLAYER 1: " + bulletsShot1 + "\n";
            str = str + "TOTAL BULLETS SHOT BY PLAYER 2: " + bulletsShot2 + "\n";
            str = str + "WAY OF VICTORY: " + wayOfVictory + "\n";
            str = str + "POSITION: " + String.format("(%d, %d)", Math.round(tanks.get(0).getPosX()), Math.round(tanks.get(0).getPosY())) + "\n";
            str = str + "DISTANCE: " + getCurrentGameStateInstance(0).getDistanceToEnemy() + "\n";
            str = str + "ANGLE TO ENEMY 1: " + getCurrentGameStateInstance(0).getAngleAtEnemy() + "\n";
            str = str + "ANGLE TO ENEMY 2: " + getCurrentGameStateInstance(1).getAngleAtEnemy() + "\n";
            str = str + "SEE 1: " + getCurrentGameStateInstance(0).getCanSeeEnemy() + "\n";
            str = str + "SEE 2: " + getCurrentGameStateInstance(1).getCanSeeEnemy() + "\n";
            return str;


        } catch (Throwable e) {
            e.printStackTrace();
            return "ERROR";

        }


    }


    public static void main(String... args) throws IOException {
        Socket clientSocket = new Socket("127.0.0.1", 12345);
        System.out.println("Connected to " + clientSocket.getRemoteSocketAddress());

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

        String userInput;
        System.out.println("Starting!");
        while (true) {
            String response = in.readLine();
            if (response == null) {
                continue;
            }
            if (response.equals("START")) {
                String first = in.readLine();
                if (first != null) {
                    if (first.equals("CGP")) {
                        first += "\n" + in.readLine();
//                        System.out.println(first);

                    } else if (first.equals("NN")) {
                        first += "\n" + in.readLine();
                    } else if (first.equals("HCINSTANCE")) {
                        in.readLine();
                    }
                }
                String second = in.readLine();
                if (second != null) {
                    if (second.equals("CGP")) {
                        second += "\n" + in.readLine();
//                        System.out.println(second);

                    } else if (second.equals("NN")) {
                        second += "\n" + in.readLine();
                    } else if (second.equals("HCINSTANCE")) {
                        in.readLine();
                    }
                }
                Integer levelIndex = 1;
                try {
                    String myLevel = in.readLine();
                    levelIndex = Integer.parseInt(myLevel.split(" ")[1]);
                } catch (Exception e) {
                    System.out.println("ERRORED");
                    e.printStackTrace();

                }
                GameInstance newGame = new GameInstance();
                String result = newGame.start(first, second, levelIndex);
//                if (result != null) System.out.println(result);
                out.println(result);
            }

        }


    }

    public boolean checkIfLinesIntersectInstance(float x1, float x2, float y1, float y2,
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

    public double calculateDistanceInstance(double x1, double y1, double x2, double y2) {
        // Calculate the squared differences
        double dx = x2 - x1;
        double dy = y2 - y1;

        // Calculate the squared distance
        double squaredDistance = dx * dx + dy * dy;

        // Calculate the distance by taking the square root
        double distance = Math.sqrt(squaredDistance);

        return distance;
    }


    public GameState getCurrentGameStateInstance(Integer tankId) {
        Tank myTank = tanks.get(tankId);
        Tank other = tanks.get(1 - tankId);

        GameState state = new GameState();
        //direct sight
        state.setAngleAtEnemy(calculateAngleFromXAxis(myTank.getPosX(), myTank.getPosY(), other.getPosX(), other.getPosY())-myTank.getAngle());
        state.setCanSeeEnemy(aCanSeeB(myTank.getPosX(), myTank.getPosY(), other.getPosX(), other.getPosY()) ? 1D : -1D);
        state.setDistanceToEnemy(calculateDistanceInstance(myTank.getPosX(), myTank.getPosY(), other.getPosX(), other.getPosY()));

        //4directions + bonus for front
        Point closest = getClosestPointInDirectionY((double) myTank.getPosX(), (double) myTank.getPosY(), (double) myTank.getAngle());
        state.setDistanceFront(calculateDistanceInstance(myTank.getPosX(), myTank.getPosY(), closest.getX(), closest.getY()));
        state.setSeesFront(aCanSeeB(closest.getX(), closest.getY(), other.getPosX(), other.getPosY()) ? 1D : -1D);

        Double wallAngle = (double) 0;
        if (walls[closest.getOrigin()].getX1() == walls[closest.getOrigin()].getX2()) {
            wallAngle = Math.PI;
        }
        state.setAngleSeesObstacle(myTank.getAngle() - wallAngle);
        state.setEnemySeesObstacle(wallAngle - calculateAngleFromXAxis(other.getPosX(), other.getPosY(), closest.getX(), closest.getY()));

        closest = getClosestPointInDirectionY((double) myTank.getPosX(), (double) myTank.getPosY(), (double) myTank.getAngle() + (Math.PI));
        state.setDistanceBack(calculateDistanceInstance(myTank.getPosX(), myTank.getPosY(), closest.getX(), closest.getY()));
        state.setSeesBack(aCanSeeB(closest.getX(), closest.getY(), other.getPosX(), other.getPosY()) ? 1D : -1D);

        closest = getClosestPointInDirectionY((double) myTank.getPosX(), (double) myTank.getPosY(), (double) myTank.getAngle() - (Math.PI / 2));
        state.setDistanceLeft(calculateDistanceInstance(myTank.getPosX(), myTank.getPosY(), closest.getX(), closest.getY()));
        state.setSeesLeft(aCanSeeB(closest.getX(), closest.getY(), other.getPosX(), other.getPosY()) ? 1D : -1D);

        closest = getClosestPointInDirectionY((double) myTank.getPosX(), (double) myTank.getPosY(), (double) myTank.getAngle() + (Math.PI / 2));
        state.setDistanceRight(calculateDistanceInstance(myTank.getPosX(), myTank.getPosY(), closest.getX(), closest.getY()));
        state.setSeesRight(aCanSeeB(closest.getX(), closest.getY(), other.getPosX(), other.getPosY()) ? 1D : -1D);

        Double radius = (double) Tank.TANK_SIZE * 1.75;
        ArrayList<Bullet> tempBullets = new ArrayList<>(bullets);
        for (Bullet bullet : tempBullets) {
            if (calculateDistanceInstance(myTank.getPosX(), myTank.getPosY(), bullet.getPosX(), bullet.getPosY()) <= radius) {
                if (calculateDistanceInstance(myTank.getPosX(), myTank.getPosY(), bullet.getPosX(), bullet.getPosY()) <= calculateDistanceInstance(myTank.getPosX(), myTank.getPosY(), bullet.getLastX(), bullet.getLastY())) {
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


    public Point getClosestPointInDirectionY(Double x, Double y, Double angleY) {
        Point outThere = getDotRemovedForXinDirectionY(x, y, dimX * 2, angleY);
        Point closest = null;
        for (int i = 0; i < walls.length; i++) {
            Wall w = walls[i];
            if (closest == null) {
                closest = whereLinesIntersect((float) (double) x, outThere.getX(), (float) (double) y, outThere.getY(), w.getX1(), w.getX2(), w.getY1(), w.getY2(), i);
            } else {
                Point temp = whereLinesIntersect((float) (double) x, outThere.getX(), (float) (double) y, outThere.getY(), w.getX1(), w.getX2(), w.getY1(), w.getY2(), i);
                if (temp != null && calculateDistanceInstance(x, y, temp.getX(), temp.getY()) < calculateDistanceInstance(x, y, closest.getX(), closest.getY())) {
                    closest = temp;
                }
            }
        }
        return closest;
    }
}
