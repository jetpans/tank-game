package HardCodedPlayer;

import AiPlayer.AiOutput;
import AiPlayer.GameState;
import proc.sketches.Game;
import proc.sketches.Tank;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class HardCodedPlayerByMisko {
    int GRID_COMPLEXITY=20;
    int chosenDestination=0;
    Float chosenAngle = null;
    int distX = Game.dimX/GRID_COMPLEXITY;
    int distY = Game.dimY/GRID_COMPLEXITY;
    int lastMovement=0;
    int lastX = 0;
    int lastY = 0;
    ArrayList<Point> allDestinations = generateAllDestinations();
    public AiOutput makeAction(GameState state) {
        lastMovement++;
        Tank myTank = Game.tanks.get(Game.hardcodedPlayerId);
        Tank otherTank = Game.tanks.get(Math.abs(1-Game.hardcodedPlayerId));
         if(aimedAtEnemy()) {
            chosenDestination=0;
            chosenAngle = null;
            return new AiOutput("FIRE","STOP_ANGULAR","STOP_LINEAR");
        }
        if(couldAimAtEnemy()) {
            chosenDestination=0;
            chosenAngle = (float) Game.calculateAngleFromXAxis(myTank.getPosX(),myTank.getPosY(),otherTank.getPosX(),otherTank.getPosY());
            String chosenDirection = getDirectionFromNeededAngle(myTank.getAngle(),chosenAngle);
            return new AiOutput("NO_FIRE",chosenDirection,"STOP_LINEAR");
        }
        if (lastMovement>1000 && (Math.abs(lastX-myTank.getPosX())>10 || Math.abs(lastY-myTank.getPosY())>10)) {
            lastMovement=0;
            lastX = (int) myTank.getPosX();
            lastY = (int) myTank.getPosY();
        }
        else if(lastMovement>1000) {
            lastMovement=0;
            lastX = (int) myTank.getPosX();
            lastY = (int) myTank.getPosY();
            ArrayList<Point> canGetThereSeesTarget = generateBestDestinations(myTank,otherTank);
            if (!canGetThereSeesTarget.isEmpty()) {
                chosenDestination =  canGetThereSeesTarget.get(ThreadLocalRandom.current().nextInt(0, canGetThereSeesTarget.size())).getOrigin();
                chosenAngle = null;
            } else {
                ArrayList<Point> canGetThere = generateOkayishDestinations(myTank);
                chosenDestination =canGetThere.get(ThreadLocalRandom.current().nextInt(0, canGetThere.size())).getOrigin();
                chosenAngle= null;
            }

            return new AiOutput("NO_FIRE","STOP_ANGULAR","STOP_LINEAR");
        }
        if( chosenDestination != 0 &&
                Math.abs(myTank.getPosX()-getXfromDest(chosenDestination))<10  && Math.abs(myTank.getPosY()-getYfromDest(chosenDestination))<10)
        {
            chosenAngle = null;
            chosenDestination = 0;
            return new AiOutput("NO_FIRE","STOP_ANGULAR","FORWARD");
        }
        if( chosenDestination != 0 &&
                Math.abs(Game.calculateAngleFromXAxis(myTank.getPosX(),myTank.getPosY(),getXfromDest(chosenDestination),getYfromDest(chosenDestination))-myTank.getAngle())<0.06f)
        {
            chosenAngle = null;
            return new AiOutput("NO_FIRE","STOP_ANGULAR","FORWARD");
        }
        if( chosenDestination != 0 &&
                Math.abs(Game.calculateAngleFromXAxis(myTank.getPosX(),myTank.getPosY(),getXfromDest(chosenDestination),getYfromDest(chosenDestination))-myTank.getAngle())>0.04f)
        {
            chosenAngle = (float) Game.calculateAngleFromXAxis(myTank.getPosX(),myTank.getPosY(),getXfromDest(chosenDestination),getYfromDest(chosenDestination));
            String chosenDirection = getDirectionFromNeededAngle(myTank.getAngle(),chosenAngle);
            return new AiOutput("NO_FIRE",chosenDirection,"STOP_LINEAR");
        }


        ArrayList<Point> canGetThereSeesTarget = generateBestDestinations(myTank,otherTank);
         if (!canGetThereSeesTarget.isEmpty()) {
            chosenDestination =  canGetThereSeesTarget.get(ThreadLocalRandom.current().nextInt(0, canGetThereSeesTarget.size())).getOrigin();
            chosenAngle = null;
        } else {
            ArrayList<Point> canGetThere = generateOkayishDestinations(myTank);
            chosenDestination =canGetThere.get(ThreadLocalRandom.current().nextInt(0, canGetThere.size())).getOrigin();
            chosenAngle= null;
        }

        return new AiOutput("NO_FIRE","STOP_ANGULAR","STOP_LINEAR");
    }

    private ArrayList<Point> generateAllDestinations() {
        ArrayList<Point> result = new ArrayList<>();
        for (int i=1; i<=GRID_COMPLEXITY*GRID_COMPLEXITY;i++) {
            result.add(new Point(getXfromDest(i),getYfromDest(i),i));
        }
        return result;
    }
    private ArrayList<Point> generateBestDestinations(Tank myThank,Tank otherTank) {
        ArrayList<Point> result= new ArrayList<>();
        for (Point p : allDestinations) {
            if (Game.aCanSeeB(myThank.getPosX(),myThank.getPosY(),p.getX(),p.getY()) &&
            Game.aCanSeeB(p.getX(),p.getY(),otherTank.getPosX(),otherTank.getPosY())) {
                result.add(p);
            }
        }
        System.out.println(result);
        return result;
    }
    private ArrayList<Point> generateOkayishDestinations(Tank myThank) {
        ArrayList<Point> result= new ArrayList<>();
        for (Point p : allDestinations) {
            if (Game.aCanSeeB(myThank.getPosX(),myThank.getPosY(),p.getX(),p.getY())) {
                result.add(p);
            }
        }
        System.out.println("OK: "+result);
        return result;
    }

    private int getYfromDest(int chosenDestination) {
        return distY/2+(distY * (chosenDestination / GRID_COMPLEXITY));
    }

    private int getXfromDest(int chosenDestination) {
        return distX/2+(distX* (chosenDestination % GRID_COMPLEXITY));

    }

    private String getDirectionFromNeededAngle(float angle, Float chosenAngle) {
        if (angle <0) {
            angle = (float) (2*Math.PI+angle);
        }
        if (chosenAngle <0) {
            chosenAngle = (float) (2*Math.PI+chosenAngle);
        }
        if(angle>=chosenAngle) {
            if(angle-chosenAngle<=Math.PI) {
                return "RIGHT";
            }
            else {
                return "LEFT";
            }
        } else {
            if(chosenAngle-angle<Math.PI) {
                return "LEFT";
            }
            else {
                return "RIGHT";
            }
        }
    }

    private boolean couldAimAtEnemy() {
        Tank myTank = Game.tanks.get(Game.hardcodedPlayerId);
        Tank otherTank = Game.tanks.get(Math.abs(1-Game.hardcodedPlayerId));
        if (Game.aCanSeeB(myTank.getPosX(),myTank.getPosY(),otherTank.getPosX(),otherTank.getPosY())) {
            return true;
        }
        return false;
    }

    private boolean aimedAtEnemy() {
        Tank myTank = Game.tanks.get(Game.hardcodedPlayerId);
        Tank otherTank = Game.tanks.get(Math.abs(1-Game.hardcodedPlayerId));
        if (Math.abs(Game.calculateAngleFromXAxis(myTank.getPosX(),myTank.getPosY(),otherTank.getPosX(),otherTank.getPosY())-myTank.getAngle())<0.04f
        && Game.aCanSeeB(myTank.getPosX(),myTank.getPosY(),otherTank.getPosX(),otherTank.getPosY())) {
            return true;
        }
        return false;
    }
}

class Point {
    int x;
    int y;
    int origin;

    public Point(int x, int y,int origin) {
        this.x = x;
        this.y = y;
        this.origin= origin;
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