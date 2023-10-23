package proc.sketches;

import processing.core.PApplet;

import java.util.ArrayList;

public class Game extends PApplet {
    public static final int dimX = 800;
    public static final int dimY = 800;
    ArrayList<Tank> tanks = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();

    public void settings() {
        size(dimX, dimY);

        tanks.add(new Tank(dimX / 2, dimY / 2, 0, "What"));
    }

    public void draw() {
        background(255, 255, 255);
        for (Tank tank : tanks) {
            tank.update(1);
            showTank(tank);
        }

        for (Bullet bullet : bullets) {
            bullet.update(1);
            showBullet(bullet);
        }
    }


    public void showTank(Tank t) {
        rectMode(CENTER);
        fill(255, 0, 0);

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


    public void keyPressed() {
        switch (key) {
            case 'w':
                tanks.get(0).forward();
                break;
            case 'a':
                tanks.get(0).left();
                break;
            case 's':
                tanks.get(0).backward();
                break;
            case 'd':
                tanks.get(0).right();
                break;
            case 'q':
                bullets.add(tanks.get(0).fireBullet());
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
        }
    }

    public static void main(String... args) {
        PApplet.main("proc.sketches.Game");
    }
}
