package proc.sketches;

import AiPlayer.AiOutput;
import AiPlayer.GameState;
import AiPlayer.PlayerAi;
import processing.core.PApplet;

import java.awt.*;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Game extends PApplet {
    public static final int dimX = 800;
    public static final int dimY = 800;
    ArrayList<Tank> tanks = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Bullet> deadBullets = new ArrayList<>();
    ArrayList<Bullet> newBullets = new ArrayList<>();
    HashSet<Object> activeKeys = new HashSet<>();

    Wall[] walls = {new Wall(5, 5, dimX - 5, 5),
            new Wall(5, 5, 5, dimY - 5),
            new Wall(dimX - 5, 5, dimX - 5, dimY - 5),
            new Wall(5, dimY - 5, dimX - 5, dimY - 5),
            new Wall(dimX / 2, 150, dimX / 2, 650),
            new Wall(150, dimY / 2, 650, dimY / 2)};
    Level myLevel = new Level(walls);

    public void settings() {
        Thread playerOneThread = new Thread(new Runnable() {
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
        playerOneThread.start();
        Thread playerTwoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    playerAi(null,0);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (AWTException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        playerTwoThread.start();

        size(dimX, dimY);
        int[] redColor = {160, 20, 10};
        int[] blueColor = {5, 5, 120};
        tanks.add(new Tank(dimX / 3, dimY / 3, 0, redColor));
        tanks.add(new Tank(dimX * 2 / 3, dimY * 2 / 3, -(float) Math.PI, blueColor));
    }

    public void draw() {
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
            }
            tank.update(1, myLevel);
            showTank(tank);
        }

        for (Wall w : myLevel.getWalls()) {
            showWall(w);
        }
        if (newBullets.size() != 0) {
            bullets.addAll(newBullets);
            newBullets.clear();
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
        activeKeys.remove(key);
        activeKeys.remove(keyCode);
        if (key == CODED) {
            System.out.println(activeKeys);
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
        PApplet.main("proc.sketches.Game");
    }

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
                        //out.println(getStateString());
                        break;
                }
                System.out.println(action);
            }

        }
    }

    GameState getCurrentGameState(Integer tankId) {
        /*
        TODO: Generate Current game state for input in AI
         */
        return new GameState();
    }
    void playerAi(Path playerBrain,Integer tankId) throws IOException, AWTException, InterruptedException {
        PlayerAi player = PlayerAi.loadFromFile(playerBrain);
        while (true) {
            System.out.println("decision on player:" + tankId);
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
}
