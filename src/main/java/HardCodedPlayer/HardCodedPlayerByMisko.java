package HardCodedPlayer;

import AiPlayer.AiOutput;
import AiPlayer.GameState;
import proc.sketches.Game;
import proc.sketches.Tank;

public class HardCodedPlayerByMisko {
    int chosenDestination=0;
    Integer chosenAngle = null;

    public AiOutput makeAction(GameState state) {

        if(aimedAtEnemy()) {
            chosenDestination=0;
            chosenAngle = null;
            return new AiOutput("FIRE","STOP_ANGULAR","STOP_LINEAR");
        }

        return new AiOutput("NO_FIRE","RIGHT","STOP_LINEAR");
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
