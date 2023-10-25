package proc.sketches;

import processing.core.PApplet;

import java.util.ArrayList;

public class Tank extends PApplet {
    private float posX;
    private float posY;
    private float angle;
    private int[] color;
    private float velocity;
    private float angularVelocity;
    public static float DEFAULT_LINEAR_VELOCITY = 4;
    public static float DEFAULT_ANGULAR_VELOCITY = 0.1f;
    public static float BARREL_SIZE = 105;
    public static float TANK_SIZE = 85;
    public final float SPAWN_LOCATION_X;
    public final float SPAWN_LOCATION_Y;

    public Tank(float posX, float posY, float angle, int[] color) {
        this.posX = posX;
        this.posY = posY;
        SPAWN_LOCATION_X = posX;
        SPAWN_LOCATION_Y = posY;
        this.color = color;
        this.angle = angle;
        this.velocity = 0;
    }


    public void forward() {
        this.velocity = DEFAULT_LINEAR_VELOCITY;
    }

    public void backward() {
        this.velocity = -DEFAULT_LINEAR_VELOCITY;
    }

    public void stop() {
        this.velocity = 0;
    }

    public void left() {
        this.angularVelocity = DEFAULT_ANGULAR_VELOCITY;
    }

    public void right() {
        this.angularVelocity = -DEFAULT_ANGULAR_VELOCITY;
    }

    public void angleStop() {
        this.angularVelocity = 0;
    }

    public void update(double timeDelta, Level level) {
        double deltaX = this.velocity * Math.cos(this.angle) * timeDelta;
        double deltaY = this.velocity * Math.sin(this.angle) * timeDelta;
        float oldPosX = this.posX;
        float oldPosY = this.posY;
        this.posX += deltaX;

        for (Wall w : level.getWalls()) {
            CollisionType t = getCollisionAxis(this, w);
            if (t == CollisionType.X || t == CollisionType.TOTAL) {
                this.posX = oldPosX;
            }

        }
        this.posY += deltaY;
        for (Wall w : level.getWalls()) {
            CollisionType t = getCollisionAxis(this, w);
            if (t == CollisionType.Y || t == CollisionType.TOTAL) {
                this.posY = oldPosY;
            }

        }

        // Update the angle based on angular velocity
        this.angle += this.angularVelocity * timeDelta;
    }


    public Bullet fireBullet() {
        return new Bullet(this.posX + (float) (TANK_SIZE * Math.cos(angle)) + velocity, this.posY + (float) (TANK_SIZE * Math.sin(angle)) + velocity, this.angle, this);
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getAngle() {
        return angle;
    }

    public int[] getColor() {
        return color;
    }

    public float getVelocity() {
        return velocity;
    }


    public static CollisionType getCollisionAxis(Tank tank, Wall wall) {
        double lX = tank.getPosX() - Tank.TANK_SIZE / 2;
        double rX = tank.getPosX() + Tank.TANK_SIZE / 2;

        double uY = tank.getPosY() - Tank.TANK_SIZE / 2;
        double bY = tank.getPosY() + Tank.TANK_SIZE / 2;


        if (wall.getType() == WallType.HORIZONTAL) {
            double Y = wall.getY1();
            double X1 = wall.getX1();
            double X2 = wall.getX2();
            if (((rX >= X1 && lX <= X1) || (lX <= X2 && rX >= X2))
                    && uY <= Y && bY >= Y) {

                if (
                        uY <= Y && bY >= Y && (rX >= X1 && lX <= X2)
                ) {
                    return CollisionType.TOTAL;
                }
                return CollisionType.X;
            }

            if (
                    uY <= Y && bY >= Y && (rX >= X1 && lX <= X2)
            ) {
                return CollisionType.Y;
            }


        } else {
            double X = wall.getX1();
            double Y1 = wall.getY1();
            double Y2 = wall.getY2();
            if (
                    (((bY >= Y1 && uY <= Y1) || (bY >= Y2 && uY <= Y2)) && lX <= X && rX >= X)
            ) {
                if (
                        lX <= X && rX >= X && uY <= Y2 && bY >= Y1
                ) {
                    return CollisionType.TOTAL;
                }
                return CollisionType.Y;
            }
            if (
                    lX <= X && rX >= X && uY <= Y2 && bY >= Y1
            ) {
                return CollisionType.X;
            }


        }
        return CollisionType.NONE;
    }

    public Bullet collideWithBullets(ArrayList<Bullet> bullets) {
        for (Bullet b : bullets) {
            if ((posX - b.getPosX()) * (posX - b.getPosX()) + (posY - b.getPosY()) * (posY - b.getPosY()) <= Tank.TANK_SIZE * Tank.TANK_SIZE / 4) {
                return b;
            }
        }
        return null;
    }

    public void respawn() {
        this.posX = SPAWN_LOCATION_X;
        this.posY = SPAWN_LOCATION_Y;
    }

}
