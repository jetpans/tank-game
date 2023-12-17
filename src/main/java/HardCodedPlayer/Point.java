package HardCodedPlayer;

public class Point {
    int x;
    int y;
    int origin;

    public Point(int x, int y, int origin) {
        this.x = x;
        this.y = y;
        this.origin = origin;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", origin=" + origin +
                '}';
    }
}
