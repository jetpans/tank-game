package proc.sketches;

public class Level {
    private final Wall[] walls;

    public Level(Wall[] walls) {
        this.walls = walls;
    }


    public Wall[] getWalls() {
        return walls;
    }
}
