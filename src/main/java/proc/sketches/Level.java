package proc.sketches;

public class Level {
    private Wall[] walls;

    public Level(Wall[] walls) {
        this.walls = walls;
    }


    public Wall[] getWalls() {
        return walls;
    }
    public void setWalls(Wall[] walls) {
        this.walls = walls;
    }
}
