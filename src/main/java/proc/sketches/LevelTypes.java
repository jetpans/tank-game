package proc.sketches;

public class LevelTypes { 
    public static int dimX = 1000;
    public static int dimY = 1000;
    
    public static void setDimensions(int dimX,int dimY) {
        LevelTypes.dimX = dimX;
        LevelTypes.dimY = dimY;
    }
    public static Wall[] getWallBlueprint(Integer id) {
        if (id == 1) {
            return LevelTypes.onlyOuterWalls;
        } else if( id == 2) {
            return LevelTypes.verticalSplitWalls;
        } else if( id == 3) {
            return LevelTypes.smallBoxCenterWalls;
        } else if( id == 4) {
            return LevelTypes.tunnelsWalls;
        } else if( id == 5) {
            return LevelTypes.randomWeirdWalls;
        }
        return LevelTypes.onlyOuterWalls;
    }
    public static Wall[] onlyOuterWalls = {//id:1
            //Outer walls
            new Wall(5, 5, dimX - 5, 5),
            new Wall(5, 5, 5, dimY - 5),
            new Wall(dimX - 5, 5, dimX - 5, dimY - 5),
            new Wall(5, dimY - 5, dimX - 5, dimY - 5)
    };

    public static Wall[] verticalSplitWalls = {//id:2
            //Outer walls
            new Wall(5, 5, dimX - 5, 5),
            new Wall(5, 5, 5, dimY - 5),
            new Wall(dimX - 5, 5, dimX - 5, dimY - 5),
            new Wall(5, dimY - 5, dimX - 5, dimY - 5),
            //lines
            new Wall(dimX/2,0,dimX/2,dimY/2-100),
            new Wall(dimX/2,dimY,dimX/2,dimY/2+100)
    };

    public static Wall[] smallBoxCenterWalls = {//id:3
            //Outer walls
            new Wall(5, 5, dimX - 5, 5),
            new Wall(5, 5, 5, dimY - 5),
            new Wall(dimX - 5, 5, dimX - 5, dimY - 5),
            new Wall(5, dimY - 5, dimX - 5, dimY - 5),
            //Obstacle 2 middle square
            new Wall(dimX / 2 - 40, dimY / 2 - 40, dimX / 2 + 40, dimY / 2 - 40),
            new Wall(dimX / 2 - 40, dimY / 2 + 40, dimX / 2 + 40, dimY / 2 + 40),
            new Wall(dimX / 2 - 40, dimY / 2 - 40, dimX / 2 - 40, dimY / 2 + 40),
            new Wall(dimX / 2 + 40, dimY / 2 - 40, dimX / 2 + 40, dimY / 2 + 40)
    };

    public static Wall[] tunnelsWalls = {//id:4
            new Wall(5, 5, dimX - 5, 5),
            new Wall(5, 5, 5, dimY - 5),
            new Wall(dimX - 5, 5, dimX - 5, dimY - 5),
            new Wall(5, dimY - 5, dimX - 5, dimY - 5),
            //tunnels
            new Wall(dimX/2,dimY/2-100,dimX/2,dimY/2+100),
            new Wall(2*dimX/8,dimY/2-100,2*dimX/8,dimY/2+100),
            new Wall(3*dimX/8,dimY/2-100,3*dimX/8,dimY/2+100),
            new Wall(5*dimX/8,dimY/2-100,5*dimX/8,dimY/2+100),
            new Wall(6*dimX/8,dimY/2-100,6*dimX/8,dimY/2+100),
            new Wall(dimX /8,dimY/2-100, dimX /8,dimY/2+100),
            new Wall(7*dimX/8,dimY/2-100,7*dimX/8,dimY/2+100)
    };

    public static Wall[] randomWeirdWalls = {//id:5
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
