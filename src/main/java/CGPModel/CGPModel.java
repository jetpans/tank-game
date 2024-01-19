package CGPModel;

import AiPlayer.AiOutput;
import AiPlayer.GameState;
import javafx.scene.shape.Path;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

import CGPModel.Operators;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CGPModel {
    private List<List<Integer>> genome;
    private List<Integer> outputs;


    private CGPModel(List<List<Integer>> genome, List<Integer> outputs) {
        this.genome = genome;
        this.outputs = outputs;
    }

    public static CGPModel buildSingleFromFile(String filename) {
        try {
            List<List<Integer>> parsedData = parseJsonFromFile(filename);
            List<Integer> outputs = parsedData.get(parsedData.size() - 1);
            parsedData.remove(parsedData.size() - 1);
            List<List<Integer>> genome = parsedData;

            return new CGPModel(genome, outputs);
        } catch (Exception e) {
            System.out.println("Failed to read file");
            e.printStackTrace();
            return null;
        }
    }

    public static CGPModel buildSingleFromString(String s) {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<List<List<Integer>>>() {
            }.getType();
            List<List<Integer>> parsedData = gson.fromJson(s, type);
            List<Integer> outputs = parsedData.remove(parsedData.size() - 1);
            List<List<Integer>> genome = parsedData;
            return new CGPModel(genome, outputs);
        } catch (Exception e) {
            System.out.println("Failed to read file");
            return null;
        }
    }

    private List<Double> evaluate(List<Double> inputs) {
        List<Double> result = new ArrayList<>(this.genome.size());

        for (int i = 0; i < this.genome.size(); i++) {
            Integer in1 = genome.get(i).get(0);
            Integer in2 = genome.get(i).get(1);
            Integer operator_key = genome.get(i).get(2);

            if (in1 < 0) {
                result.add(inputs.get((-(in1 + 1)) % inputs.size()));
                continue;
            }
            try {
                result.add(i, Operators.operators.get(operator_key).apply(result.get(in1), result.get(in2)));
            } catch (Exception e) {
                System.out.println(this.genome);
                System.out.println(this.outputs);
                System.out.println(result);
                System.out.println(operator_key);
                System.out.println(result.get(in1));
                System.out.println(result.get(in2));
                e.printStackTrace();
                System.exit(0);
            }
        }
        List<Double> outputs = new ArrayList<>(this.outputs.size());
        for (Integer index : this.outputs) {
            outputs.add(result.get(index));
        }
        return outputs;
    }

    public AiOutput makeAction(GameState state) {
        AiOutput answer = new AiOutput();
        List<Double> inputs = Arrays.asList
                (state.getAngleAtEnemy(), state.getCanSeeEnemy(), state.getDistanceToEnemy(),
                        state.getDistanceFront(), state.getDistanceBack(), state.getDistanceLeft(), state.getDistanceRight(),
                        Double.valueOf(state.getBulletsBack()), Double.valueOf(state.getBulletsFront()),
                        Double.valueOf(state.getBulletsLeft()), Double.valueOf(state.getBulletsRight())
                );
        List<Double> results = this.evaluate(inputs);
        try {
            Double fireOutput = results.get(2);
            Double linearOutput = results.get(0);
            Double angularOutput = results.get(1);
            if (fireOutput > 0) {
                answer.setFireDecision("FIRE");
            } else {
                answer.setFireDecision("");
            }

            if (linearOutput < 0) {
                answer.setLinearDecision("BACKWARD");
            } else if (linearOutput >= 0) {
                answer.setLinearDecision("FORWARD");
            } else {
                answer.setLinearDecision("STOP_LINEAR");
            }

            if (angularOutput < -0.5) {
                answer.setAngularDecision("LEFT");
            } else if (angularOutput >= 0.5) {
                answer.setAngularDecision("RIGHT");
            } else {
                answer.setAngularDecision("STOP_ANGULAR");
            }
            return answer;
        } catch (Exception e) {
            System.out.println("Wrong genome");
            return null;
        }


    }

    public static List<List<Integer>> parseJsonFromFile(String filePath) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<List<List<Integer>>>() {
        }.getType();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            return gson.fromJson(reader, type);
        }
    }

    public static void main(String[] args) {
        //Debug test
        System.out.println("Hello");
        CGPModel myModel = buildSingleFromFile("D:\\Coding\\FAKS\\Mentor\\TankGame\\tank-game\\python\\Cartesian_GP\\result0.txt");

        List<List<Integer>> myGenome = myModel.getGenome();
        System.out.println(myGenome.get(0).get(0));
//        System.out.println(myModel.getGenome());
    }

    public List<List<Integer>> getGenome() {
        return genome;
    }

    public List<Integer> getOutputs() {
        return outputs;
    }

}
