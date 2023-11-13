package HardCodedPlayer;

import AiPlayer.AiOutput;
import AiPlayer.GameState;
import proc.sketches.Game;
import proc.sketches.Tank;

public class HardCodedPlayerByMisko {
    int chosenDestination=0;
    Float chosenAngle = null;

    public AiOutput makeAction(GameState state) {

        if(aimedAtEnemy()) {
            chosenDestination=0;
            chosenAngle = null;
            return new AiOutput("FIRE","STOP_ANGULAR","STOP_LINEAR");
        }
        if(couldAimAtEnemy()) {
            chosenDestination=0;
            Tank myTank = Game.tanks.get(Game.hardcodedPlayerId);
            Tank otherTank = Game.tanks.get(Math.abs(1-Game.hardcodedPlayerId));
            chosenAngle = (float) Game.calculateAngleFromXAxis(myTank.getPosX(),myTank.getPosY(),otherTank.getPosX(),otherTank.getPosY());
            System.out.println(myTank.getAngle()+" "+chosenAngle);
            String chosenDirection = getDirectionFromNeededAngle(myTank.getAngle(),chosenAngle);
            System.out.println(chosenDirection);
            return new AiOutput("NO_FIRE",chosenDirection,"STOP_LINEAR");
        }

        return new AiOutput("NO_FIRE","STOP_ANGULAR","STOP_LINEAR");
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
            if(chosenAngle-angle<=Math.PI) {
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
            System.out.println("Could See him");
            return true;
        }
        return false;
    }

    private boolean aimedAtEnemy() {
        Tank myTank = Game.tanks.get(Game.hardcodedPlayerId);
        Tank otherTank = Game.tanks.get(Math.abs(1-Game.hardcodedPlayerId));
        if (Math.abs(Game.calculateAngleFromXAxis(myTank.getPosX(),myTank.getPosY(),otherTank.getPosX(),otherTank.getPosY())-myTank.getAngle())<0.02f
        && Game.aCanSeeB(myTank.getPosX(),myTank.getPosY(),otherTank.getPosX(),otherTank.getPosY())) {
            System.out.println("Found");
            return true;
        }
        return false;
    }
}
