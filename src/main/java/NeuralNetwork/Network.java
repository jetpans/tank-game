package NeuralNetwork;
import AiPlayer.AiOutput;
import AiPlayer.GameState;

import java.io.*;
import java.util.*;

public class Network
{
    ArrayList<Layer> layers;
    int inputDimension;
    int outcomeDimension;
    Layer finalLayer;
    double fitness;

    public Network(File file) {
        BufferedReader brTest = null;
        try {
            brTest = new BufferedReader(new FileReader(file));
            String line = brTest.readLine();
            String numOfLayers = brTest.readLine();
            brTest.readLine();

            layers = new ArrayList<>();
            for (int i = 0; i<Integer.parseInt(numOfLayers)-1; i++) {
                List<String> matrica = new ArrayList<>();
                while(!(line= brTest.readLine()).equals("#####")) {
                    matrica.add(line);
                }
                double[][] fakeMatrix = new double[matrica.size()][matrica.get(0).split(" ").length];
                for (int j= 0; j< matrica.size();j++) {
                    double[] linija = Arrays.stream(matrica.get(j).split(" "))
                            .mapToDouble(Double::parseDouble)
                            .toArray();
                    for (int k= 0;k<linija.length;k++) {
                        fakeMatrix[j][k] = linija[k];
                    }
                }
                RealMatrix matrix = new RealMatrix(fakeMatrix);
                layers.add(new Layer(matrix.getColumnDimension(), matrix.getRowDimension(), matrix));
            }
            List<String> matrica = new ArrayList<>();
            while(!(line= brTest.readLine()).equals("#####")) {
                matrica.add(line);
            }
            double[][] fakeMatrix = new double[matrica.size()][matrica.get(0).split(" ").length];
            for (int j= 0; j< matrica.size();j++) {
                double[] linija = Arrays.stream(matrica.get(j).split(" "))
                        .mapToDouble(Double::parseDouble)
                        .toArray();
                for (int k= 0;k<linija.length;k++) {
                    fakeMatrix[j][k] = linija[k];
                }
            }
            RealMatrix matrix = new RealMatrix(fakeMatrix);
            finalLayer = new Layer(matrix.getColumnDimension(), matrix.getRowDimension(), matrix);
            outcomeDimension = finalLayer.getNumberOfNeurons();
            inputDimension = layers.get(0).getInputDimension();
        } catch (IOException e) {}

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
        return  v;
    }

    private double customFunction(double v)
    {
        return (1d/(1d+Math.exp(0-v)));
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

    public AiOutput makeAction(GameState currentGameState) {
        /*
        todo : make transmision from state to input
        todo : make transmision from output to Action
         */
        return null;
    }
}
