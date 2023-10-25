package proc.sketches;


public class Bullet {
    private float posX;
    private float posY;
    public static final int MAX_LIFE = 300;
    private int currentLife = 0;
    public static float VELOCITY = 10;
    public static float SIZE = 10;
    private float angle;
    private float velY;
    private float velX;

    Tank owner;

    public Bullet(float posX, float posY, float angle, Tank owner) {
        this.posX = posX;
        this.posY = posY;
        this.velY = (float) (VELOCITY * Math.sin(angle));
        this.velX = (float) (VELOCITY * Math.cos(angle));
        this.owner = owner;
    }

    public void update(float timeDelta, Level level) {
        currentLife += timeDelta;
        double deltaX = velX * timeDelta;
        double deltaY = velY * timeDelta;
        double newX = posX + deltaX;
        double newY = posY + deltaY;
        for (Wall w : level.getWalls()) {
            if (w.getType() == WallType.HORIZONTAL) {
                if (posX >= w.getX1() && posX <= w.getX2()) {
                    if (posY < w.getY1() && newY >= w.getY1()) {
                        this.velY = -this.velY;
                        break;
                    } else if (posY > w.getY1() && newY <= w.getY1()) {
                        this.velY = -this.velY;
                        break;
                    }
                }

            } else {
                if (posY >= w.getY1() && posY <= w.getY2()) {
                    if (posX < w.getX1() && newX >= w.getX1()) {
                        this.velX = -this.velX;
                        break;
                    } else if (posX >= w.getX1() && newX <= w.getX1()) {
                        this.velX = -this.velX;
                        break;
                    }
                }
            }
        }
        deltaX = velX * timeDelta;
        deltaY = velY * timeDelta;
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

    public boolean shouldIDie() {
        return currentLife >= MAX_LIFE;
    }

}