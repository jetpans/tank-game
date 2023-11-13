package AiPlayer;

import HardCodedPlayer.HardCodedPlayerByMisko;
import proc.sketches.Game;

import java.nio.file.Path;

public class PlayerAi {
    String type = "DUMMY";

    //here are paremeters for Hardcoded player
    HardCodedPlayerByMisko hardCode;

    //here are neural network parameters

    //TODO here add a new brain type and its parameters


    public static PlayerAi loadFromFile(Path playerBrain, int id) {
        //TODO read first line of file: type of brain

        //TODO: if else --> way to read and save each type of brain
        PlayerAi playerAi = new PlayerAi();
        if(playerBrain==null) {
            return new PlayerAi();
        }
        if ("AAA".equals(playerBrain.toString())) {
            playerAi.type = "HARDCODED";
            playerAi.hardCode = new HardCodedPlayerByMisko();
            Game.hardcodedPlayerId = id;
        }
        return playerAi;
    }


    public AiOutput makeDecisionBasedOnGameState(GameState currentGameState) {
        /*
        TODO if else -->> based on type of brain make decision
         */
        if (type.equals("DUMMY")) {
            return new AiOutput("NO_FIRE","RIGHT","STOP_LINEAR");
        } else if (type.equals("HARDCODED")) {
            return hardCode.makeAction(currentGameState);
        }
        return new AiOutput("NO_FIRE","RIGHT","STOP_LINEAR");
    }
}
