package proc.sketches;


public class Bullet {
    private float posX;
    private float posY;

    public transient static final int MAX_LIFE = 300;
    public transient static final int GRACE_PERIOD = 30;
    private transient int currentLife = 0;
    public transient static float VELOCITY = 10;
    public transient static float SIZE = 10;


    private transient float lastX;
    private transient float lastY;
    private float angle;

    private float velY;
    private float velX;

    Tank owner;

    public Bullet(float posX, float posY, float angle, Tank owner) {
        this.posX = posX;
        this.posY = posY;
        this.lastX = owner.getPosX();
        this.lastY = owner.getPosY();
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
        this.lastX=this.posX;
        this.lastY=this.posY;
        this.posX += deltaX;
        this.posY += deltaY;
    }


    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public Tank getOwner() {
        return owner;
    }

    public int getCurrentLife() {
        return currentLife;
    }

    public void setCurrentLife(int currentLife) {
        this.currentLife = currentLife;
    }

    public boolean shouldIDie() {
        return currentLife >= MAX_LIFE;
    }

    public float getLastX() {
        return lastX;
    }

    public void setLastX(float lastX) {
        this.lastX = lastX;
    }

    public float getLastY() {
        return lastY;
    }

    public void setLastY(float lastY) {
        this.lastY = lastY;
    }
}
