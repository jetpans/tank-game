package AiPlayer;

import java.nio.file.Path;

public class PlayerAi {

    //here are neural network parameters

    //TODO here add a new brain type and its parameters


    public static PlayerAi loadFromFile(Path playerBrain) {
        //TODO read first line of file: type of brain

        //TODO: if else --> way to read and save each type of brain
        return new PlayerAi();
    }


    public AiOutput makeDecisionBasedOnGameState(GameState currentGameState) {
        /*
        TODO if else -->> based on type of brain make decision
         */
        return new AiOutput("NO_FIRE","RIGHT","FORWARD");
    }
}
