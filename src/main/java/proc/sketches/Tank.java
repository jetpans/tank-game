package proc.sketches;

import processing.core.PApplet;

public class Tank extends PApplet {
    private float posX;
    private float posY;
    private float angle;
    private String color;
    private float velocity;
    private float angularVelocity;
    public static float DEFAULT_LINEAR_VELOCITY = 4;
    public static float DEFAULT_ANGULAR_VELOCITY = 0.1f;
    public static float BARREL_SIZE = 105;
    public static float TANK_SIZE = 85;

    public Tank(float posX, float posY, float angle, String color) {
        this.posX = posX;
        this.posY = posY;
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

    public void update(double timeDelta) {
        double deltaX = this.velocity * Math.cos(this.angle) * timeDelta;
        double deltaY = this.velocity * Math.sin(this.angle) * timeDelta;

        this.posX += deltaX;
        this.posY += deltaY;

        // Update the angle based on angular velocity
        this.angle += this.angularVelocity * timeDelta;
    }


    public Bullet fireBullet() {
        return new Bullet(this.posX, this.posY, this.angle, this);
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

    public String getColor() {
        return color;
    }

    public float getVelocity() {
        return velocity;
    }
}
