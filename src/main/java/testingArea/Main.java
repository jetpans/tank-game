package testingArea;

import NeuralNetwork.Network;

import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String... args) {
        Instant realStart = Instant.now();
        Network proba = new Network(new File("C:\\Users\\mpristav\\IdeaProjects\\tank-game\\src\\main\\resources\\AIplayersBrains\\nekiBasicNN"));
        Instant start = Instant.now();
        Double b=0D;
        for (int i = 0; i<1000000;i++) {
            b+=proba.predict(new ArrayList<>(Arrays.asList(0.1,0.1,0.1,0.1,0.1))).get(0);
        }
        Instant end = Instant.now();
        System.out.println(ChronoUnit.MILLIS.between(start,end));
        System.out.println(ChronoUnit.MILLIS.between(realStart,end));
    }
}
