package proc.sketches;

public class Bullet {
    private float posX;
    private float posY;
    public static float VELOCITY = 10;
    public static float SIZE = 10;
    private final float angle;
    Tank owner;

    public Bullet(float posX, float posY, float angle, Tank owner) {
        this.posX = posX;
        this.posY = posY;
        this.angle = angle;
        this.owner = owner;
    }

    public void update(float timeDelta) {
        double deltaX = VELOCITY * Math.cos(this.angle) * timeDelta;
        double deltaY = VELOCITY * Math.sin(this.angle) * timeDelta;

        this.posX += deltaX;
        this.posY += deltaY;

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

    public Tank getOwner() {
        return owner;
    }
}
