package AiPlayer;

import CGPModel.CGPModel;
import HardCodedPlayer.HardCodedPlayerByMisko;
import NeuralNetwork.Network;

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
    Network neuralNetwork;

    CGPModel modelCGP;
    //TODO here add a new brain type and its parameters


    public static PlayerAi loadFromFile(Path playerBrain, int id) throws IOException {
        BufferedReader brTest = null;
        String firstLine = null;
        try {
            brTest = new BufferedReader(new FileReader(playerBrain.toFile()));
            firstLine = brTest.readLine();
        } catch (FileNotFoundException e) {
            playerBrain = null;
        }


        //TODO: if else --> way to read and save each type of brain
        PlayerAi playerAi = new PlayerAi();
        playerAi.type = "DUMMY";
        if (playerBrain == null) {
            playerAi.type = "DUMMY";
            return playerAi;
        } else if ("HARDCODED MISKO".equals(firstLine)) {
            playerAi.type = "HARDCODED";
            playerAi.hardCode = new HardCodedPlayerByMisko(20, id);
        } else if ("NN".equals(firstLine)) {
            playerAi.type = "NN";
            playerAi.neuralNetwork = new Network(playerBrain.toFile());
        } else if (firstLine.equals("CGP")) {
            playerAi.type = "CGP";
            playerAi.modelCGP = CGPModel.buildSingleFromFile(playerBrain.toString());
        }
        return playerAi;
    }

    public static PlayerAi loadFromString(String playerBrain, int id) {
        String firstLine = null;

        firstLine = playerBrain.split("\n")[0];
        playerBrain = playerBrain.substring(playerBrain.indexOf("\n") + 1);


        //TODO: if else --> way to read and save each type of brain
        PlayerAi playerAi = new PlayerAi();
        playerAi.type = "DUMMY";

        if (playerBrain == null) {
            playerAi.type = "DUMMY";
            return playerAi;
        } else if ("HARDCODED MISKO".equals(firstLine)) {
            //TODO fix this
        } else if ("NN".equals(firstLine)) {
            //TODO parse NN from String somehow.
        } else if (firstLine.equals("CGP")) {
            playerAi.type = "CGP";
            playerAi.modelCGP = CGPModel.buildSingleFromString(playerBrain);
        }
        System.out.println("Created " + playerAi.type);
        return playerAi;

    }

    public AiOutput makeDecisionBasedOnGameState(GameState currentGameState) {
        /*
        TODO if else -->> based on type of brain make decision
         */
        if (type.equals("DUMMY")) {
            return new AiOutput("NO_FIRE", "RIGHT", "STOP_LINEAR");
        } else if (type.equals("HARDCODED")) {
            return hardCode.makeAction();
        } else if (type.equals("NN")) {
            return neuralNetwork.makeAction(currentGameState);
        } else if (type.equals("CGP")) {
            return modelCGP.makeAction(currentGameState);
        }
        return new AiOutput("NO_FIRE", "RIGHT", "STOP_LINEAR");
    }
}
