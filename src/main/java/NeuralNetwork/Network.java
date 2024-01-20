package NeuralNetwork;

import AiPlayer.AiOutput;
import AiPlayer.GameState;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Network
{
    ArrayList<Layer> layers = new ArrayList<>();
    int inputDimension;
    int outcomeDimension;
    Layer finalLayer;
    double fitness;

    public Network(File file) {
        BufferedReader brTest = null;
        try {
            brTest = new BufferedReader(new FileReader(file));
            String line = brTest.readLine();
            String playerBrain = brTest.readLine();

            Map<String,Object> result =
                    new ObjectMapper().readValue(playerBrain, HashMap.class);
            this.inputDimension= (int) result.get("input_dim");
            this.outcomeDimension= (int) result.get("output_dim");
            ArrayList<LinkedHashMap<String,Object>> layers = (ArrayList<LinkedHashMap<String, Object>>) result.get("layers");
            for (int i=0; i<layers.size()-1;i++) {
                int input = (int) layers.get(i).get("cols");
                int output = (int) layers.get(i).get("rows");
                double[][] matrica = new double[output][input];
                for (int j=0;j<input;j++) {
                    for (int k=0;k<output;k++) {
                        matrica[k][j]=((ArrayList<ArrayList<Double>>)layers.get(i).get("data")).get(k).get(j);
                    }
                }
                Layer currentLayer = new Layer(input,output,new RealMatrix(matrica));
                this.layers.add(currentLayer);
            }
            int input = (int) layers.get(layers.size()-1).get("cols");
            int output = (int) layers.get(layers.size()-1).get("rows");
            double[][] matrica = new double[output][input];
            for (int j=0;j<input;j++) {
                for (int k=0;k<output;k++) {
                    matrica[k][j]=((ArrayList<ArrayList<Double>>)layers.get(layers.size()-1).get("data")).get(k).get(j);
                }
            }
            this.finalLayer = new Layer(input,output,new RealMatrix(matrica));


        } catch (IOException e) {}

    }

    public Network(String playerBrain) {
        try {
            Map<String,Object> result =
                    new ObjectMapper().readValue(playerBrain, HashMap.class);
            this.inputDimension= (int) result.get("input_dim");
            this.outcomeDimension= (int) result.get("output_dim");
            ArrayList<LinkedHashMap<String,Object>> layers = (ArrayList<LinkedHashMap<String, Object>>) result.get("layers");
            for (int i=0; i<layers.size()-1;i++) {
                int input = (int) layers.get(i).get("cols");
                int output = (int) layers.get(i).get("rows");
                double[][] matrica = new double[output][input];
                for (int j=0;j<input;j++) {
                    for (int k=0;k<output;k++) {
                        matrica[k][j]=((ArrayList<ArrayList<Double>>)layers.get(i).get("data")).get(k).get(j);
                    }
                }
                Layer currentLayer = new Layer(input,output,new RealMatrix(matrica));
                this.layers.add(currentLayer);
            }
            int input = (int) layers.get(layers.size()-1).get("cols");
            int output = (int) layers.get(layers.size()-1).get("rows");
            double[][] matrica = new double[output][input];
            for (int j=0;j<input;j++) {
                for (int k=0;k<output;k++) {
                    matrica[k][j]=((ArrayList<ArrayList<Double>>)layers.get(layers.size()-1).get("data")).get(k).get(j);
                }
            }
            this.finalLayer = new Layer(input,output,new RealMatrix(matrica));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Double> predict(ArrayList<Double> x)
    {
        double[][] xDArray=new double[x.size()+1][1];
        for(int i=0;i<x.size();i++)
        {
            xDArray[i][0]=x.get(i);
        }
        xDArray[x.size()][0]=1;
        RealMatrix xMatrix=new RealMatrix(xDArray);
        for(Layer layer:layers)
        {
            RealMatrix y= layer.getOutcomes(xMatrix);
            double[][] yDArray=new double[y.getRowDimension()+1][1];
            yDArray[y.getRowDimension()][0]=1;
            for(int i=0;i<y.getColumn(0).length;i++)
            {
                yDArray[i][0]=customFunction(y.getColumn(0)[i]);
            }
            xMatrix=new RealMatrix(yDArray);
        }
        RealMatrix y= finalLayer.getOutcomes(xMatrix);
        ArrayList<Double>result=new ArrayList<>();
        for(int i=0;i<y.getColumn(0).length;i++)
        {
            result.add(customFinalFunction(y.getColumn(0)[i]));
        }
        return result;
    }

    private Double customFinalFunction(double v)
    {
        return 1-(2*(1d/(1d+Math.exp(0-v))));
    }

    private double customFunction(double v)
    {
        return 1-(2*(1d/(1d+Math.exp(0-v))));
    }

    public ArrayList<Layer> getLayers() {
        return layers;
    }

    public void setLayers(ArrayList<Layer> layers) {
        this.layers = layers;
    }

    public int getInputDimension() {
        return inputDimension;
    }

    public void setInputDimension(int inputDimension) {
        this.inputDimension = inputDimension;
    }

    public int getOutcomeDimension() {
        return outcomeDimension;
    }

    public void setOutcomeDimension(int outcomeDimension) {
        this.outcomeDimension = outcomeDimension;
    }

    public Layer getFinalLayer() {
        return finalLayer;
    }

    public void setFinalLayer(Layer finalLayer) {
        this.finalLayer = finalLayer;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Network)) return false;
        Network network = (Network) o;
        return getInputDimension() == network.getInputDimension() && getOutcomeDimension() == network.getOutcomeDimension() && Double.compare(network.getFitness(), getFitness()) == 0 && Objects.equals(getLayers(), network.getLayers()) && Objects.equals(getFinalLayer(), network.getFinalLayer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLayers(), getInputDimension(), getOutcomeDimension(), getFinalLayer(), getFitness());
    }

    public AiOutput makeAction(GameState st) {
        ArrayList<Double> ulaz = new ArrayList<>();
        ulaz.add(st.getAngleAtEnemy());
        ulaz.add(st.getCanSeeEnemy());
        ulaz.add(st.getDistanceToEnemy());

        ulaz.add(st.getDistanceFront());
        ulaz.add(st.getSeesFront());
        ulaz.add(st.getDistanceBack());
        ulaz.add(st.getSeesBack());
        ulaz.add(st.getDistanceLeft());
        ulaz.add(st.getSeesLeft());
        ulaz.add(st.getDistanceRight());
        ulaz.add(st.getSeesRight());

        ulaz.add(st.getAngleSeesObstacle());
        ulaz.add(st.getEnemySeesObstacle());


        ulaz.add(Double.valueOf(st.getBulletsFront()));
        ulaz.add(Double.valueOf(st.getBulletsBack()));
        ulaz.add(Double.valueOf(st.getBulletsLeft()));
        ulaz.add(Double.valueOf(st.getBulletsRight()));

        List<Double> izlaz = this.predict(ulaz);

        AiOutput odluka = new AiOutput();

        odluka.setFireDecision(izlaz.get(0)>0 ? "FIRE" : "NO_FIRE");

        if (izlaz.get(1)<-0.2) {
            odluka.setAngularDecision("LEFT");
        } else if (izlaz.get(1)>0.2) {
            odluka.setAngularDecision("RIGHT");
        } else {
            odluka.setAngularDecision("STOP_ANGULAR");
        }

        if (izlaz.get(2)<-0.2) {
            odluka.setLinearDecision("FORWARD");
        } else if (izlaz.get(2)>0.2) {
            odluka.setLinearDecision("BACKWARD");
        } else {
            odluka.setLinearDecision("STOP_LINEAR");
        }

        return odluka;
    }
}
