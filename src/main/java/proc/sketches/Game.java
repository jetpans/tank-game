package proc.sketches;

import processing.core.PApplet;

import java.util.ArrayList;

public class Game extends PApplet {
    public static final int dimX = 800;
    public static final int dimY = 800;
    public static boolean isKeyPressed = false;
    ArrayList<Tank> tanks = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Bullet> deadBullets = new ArrayList<>();
    Wall[] walls = {new Wall(5, 5, dimX - 5, 5),
            new Wall(5, 5, 5, dimY - 5),
            new Wall(dimX - 5, 5, dimX - 5, dimY - 5),
            new Wall(5, dimY - 5, dimX - 5, dimY - 5),
            new Wall(dimX / 2, 150, dimX / 2, 650),
            new Wall(150, dimY / 2, 650, dimY / 2)};
    Level myLevel = new Level(walls);

    public void settings() {
        size(dimX, dimY);
        int[] redColor = {160, 20, 10};
        int[] blueColor = {5, 5, 120};
        tanks.add(new Tank(dimX / 3, dimY / 3, 0, redColor));
        tanks.add(new Tank(dimX * 2 / 3, dimY * 2 / 3, -(float) Math.PI, blueColor));
    }

    public void draw() {
        background(255, 255, 255);
        for (Tank tank : tanks) {
            Bullet res = tank.collideWithBullets(bullets);
            if (res != null) {
                tank.respawn();
                deadBullets.add(res);
            }
            tank.update(1, myLevel);
            showTank(tank);
        }

        for (Bullet bullet : bullets) {
            if (bullet.shouldIDie()) {
                deadBullets.add(bullet);
            }
            bullet.update(1, myLevel);
            showBullet(bullet);
        }
        for (Bullet i : deadBullets) {
            bullets.remove(i);
        }
        deadBullets.clear();
        for (Wall w : myLevel.getWalls()) {
            showWall(w);
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
            case ENTER:
                bullets.add(tanks.get(1).fireBullet());
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

        switch (key) {
            case 'w':
                tanks.get(0).stop();
                break;
            case 'a':
                tanks.get(0).angleStop();
                break;
            case 's':
                tanks.get(0).stop();
                break;
            case 'd':
                tanks.get(0).angleStop();
                break;
            case CODED:
                switch (keyCode) {
                    case UP:
                        tanks.get(1).stop();
                        break;
                    case LEFT:
                        tanks.get(1).angleStop();
                        break;
                    case DOWN:
                        tanks.get(1).stop();
                        break;
                    case RIGHT:
                        tanks.get(1).angleStop();
                        break;
                }
                break;
        }
    }

    public static void main(String... args) {
        PApplet.main("proc.sketches.Game");
    }
}
