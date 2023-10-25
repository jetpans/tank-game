package proc.sketches;

public class Wall {
    private float x1;
    private float x2;
    private float y1;
    private float y2;
    WallType type;

    public Wall(float x1, float y1, float x2, float y2) {
        if (x1 != x2 && y1 != y2) {
            throw new IllegalArgumentException("Wall must be either horizontal or vertical!");
        }
        if (x1 == x2) {
            this.type = WallType.VERTICAL;
        }
        if (y1 == y2) {
            this.type = WallType.HORIZONTAL;
        }
        if (x1 < x2) {
            this.x1 = x1;
            this.x2 = x2;
        } else {
            this.x1 = x2;
            this.x2 = x1;
        }

        if (y1 < y2) {
            this.y1 = y1;
            this.y2 = y2;
        } else {
            this.y1 = y2;
            this.y2 = y1;
        }


    }

    public float getX1() {
        return x1;
    }

    public float getX2() {
        return x2;
    }

    public float getY1() {
        return y1;
    }

    public float getY2() {
        return y2;
    }

    public WallType getType() {
        return this.type;
    }
}
