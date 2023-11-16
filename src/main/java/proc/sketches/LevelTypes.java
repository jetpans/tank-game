package proc.sketches;

public class LevelTypes { 
    public static int dimX = 1000;
    public static int dimY = 1000;
    
    public LevelTypes(int dimX,int dimY) {
        LevelTypes.dimX = dimX;
        LevelTypes.dimY = dimY;
    }
    public static Wall[] OnlyOuterWalls = {
            //Outer walls
            new Wall(5, 5, dimX - 5, 5),
            new Wall(5, 5, 5, dimY - 5),
            new Wall(dimX - 5, 5, dimX - 5, dimY - 5),
            new Wall(5, dimY - 5, dimX - 5, dimY - 5)
    };

    public static Wall[] SmallBoxCenterWalls = {
            //Outer walls
            new Wall(5, 5, dimX - 5, 5),
            new Wall(5, 5, 5, dimY - 5),
            new Wall(dimX - 5, 5, dimX - 5, dimY - 5),
            new Wall(5, dimY - 5, dimX - 5, dimY - 5),
            //Obstacle 2 midle square
            new Wall(dimX / 2 - 40, dimY / 2 - 40, dimX / 2 + 40, dimY / 2 - 40),
            new Wall(dimX / 2 - 40, dimY / 2 + 40, dimX / 2 + 40, dimY / 2 + 40),
            new Wall(dimX / 2 - 40, dimY / 2 - 40, dimX / 2 - 40, dimY / 2 + 40),
            new Wall(dimX / 2 + 40, dimY / 2 - 40, dimX / 2 + 40, dimY / 2 + 40)
    };
    
    public static Wall[] RandomWeirdWalls = {
            //Outer walls
            new Wall(5, 5, dimX - 5, 5),
            new Wall(5, 5, 5, dimY - 5),
            new Wall(dimX - 5, 5, dimX - 5, dimY - 5),
            new Wall(5, dimY - 5, dimX - 5, dimY - 5),
            //Obstacle 1 upper right square
            new Wall(dimX / 2, dimY / 4, dimX / 2, 3 * dimY / 8),
            new Wall(dimX / 2 + dimX / 8, dimY / 4, dimX / 2 + dimX / 8, 3 * dimY / 8),

            new Wall(dimX / 2, dimY / 4, dimX / 2 + dimX / 8, dimY / 4),
            new Wall(dimX / 2 + dimX / 8, 3 * dimY / 8, dimX / 2, 3 * dimY / 8),
            //Obstacle 2 middle square
            new Wall(dimX / 2 - 20, dimY / 2 - 20, dimX / 2 + 20, dimY / 2 - 20),
            new Wall(dimX / 2 - 20, dimY / 2 + 20, dimX / 2 + 20, dimY / 2 + 20),
            new Wall(dimX / 2 - 20, dimY / 2 - 20, dimX / 2 - 20, dimY / 2 + 20),
            new Wall(dimX / 2 + 20, dimY / 2 - 20, dimX / 2 + 20, dimY / 2 + 20),
            //Obstacle 3 left down rectangle
            new Wall(200, dimY - 100, dimX / 2, dimY - 100),
            new Wall(200, dimY - 150, dimX / 2, dimY - 150),
            new Wall(200, dimY - 100, 200, dimY - 150),
            new Wall(dimX / 2, dimY - 100, dimX / 2, dimY - 150),
            // random lines

            new Wall(250, 550, 250, 300),
            new Wall(500, 600, 500, 800)
    };
}
