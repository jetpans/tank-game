package AiPlayer;

import HardCodedPlayer.HardCodedPlayerByMisko;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class PlayerAi {
    String type = "DUMMY";

    //here are paremeters for Hardcoded player
    HardCodedPlayerByMisko hardCode;

    //here are neural network parameters

    //TODO here add a new brain type and its parameters


    public static PlayerAi loadFromFile(Path playerBrain, int id) throws IOException {
        BufferedReader brTest = null;
        try {
            brTest = new BufferedReader(new FileReader(playerBrain.toFile()));
        } catch (FileNotFoundException e) {
            playerBrain=null;
        }
        String firstLine = brTest.readLine();
        //TODO: if else --> way to read and save each type of brain
        PlayerAi playerAi = new PlayerAi();
        playerAi.type="DUMMY";
        if(playerBrain==null) {
            playerAi.type="DUMMY";
            return playerAi;
        }
        if ("HARDCODED MISKO".equals(firstLine)) {
            playerAi.type = "HARDCODED";
            playerAi.hardCode = new HardCodedPlayerByMisko(20,id);
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
            return hardCode.makeAction();
        }
        return new AiOutput("NO_FIRE","RIGHT","STOP_LINEAR");
    }
}
