package NeuralNetwork;
import java.util.*;

public class Network
{
    ArrayList<Layer> layers;
    int inputDimension;
    int outcomeDimension;
    Layer finalLayer;
    double fitness;


    public Network(ArrayList<Layer> layers, int inputDimension, int outcomeDimension,Layer finalLayer) {
       this.layers=new ArrayList<>();
        for (Layer layer : layers)
        {
            this.layers.add(new Layer(layer.getInputDimension(), layer.getNumberOfNeurons(), layer.getNeuronValues().copy()));
        }
        this.finalLayer=new Layer(finalLayer.getInputDimension(), finalLayer.getNumberOfNeurons(), finalLayer.getNeuronValues().copy());
        this.inputDimension = inputDimension;
        this.outcomeDimension = outcomeDimension;
    }

    public Network(int inputDimension, int outcomeDimension,ArrayList<Integer>RandomisedLayersDimensions) {
        this.inputDimension = inputDimension;
        this.outcomeDimension = outcomeDimension;
        this.layers=new ArrayList<>();
        int lastInnerLayerDim=createRandomLayers(RandomisedLayersDimensions);
        createFinalLayer(lastInnerLayerDim);

    }

    private int createRandomLayers(ArrayList<Integer>RandomisedLayersDimensions)
    {
        int lastInnerLayerDim=inputDimension;
        for(int i=0;i<RandomisedLayersDimensions.size();i++)
        {
            lastInnerLayerDim=RandomisedLayersDimensions.get(i);
            int input;
            if(i==0)
                input=inputDimension;
            else
                input=RandomisedLayersDimensions.get(i-1);
            double[][] wMatrix=new double[RandomisedLayersDimensions.get(i)][input+1];
            Random b=new Random();
            for(int j=0;j<RandomisedLayersDimensions.get(i);j++)
            {
                for(int k=0;k<=input;k++)
                {
                    wMatrix[j][k]=(b.nextGaussian()*0.01);
                }
            }
            this.layers.add(new Layer(input,RandomisedLayersDimensions.get(i),new RealMatrix(wMatrix)));
        }
        return lastInnerLayerDim;
    }

    private void createFinalLayer(int lastInnerLayerDim)
    {
        double[][] wMatrix=new double[outcomeDimension][lastInnerLayerDim+1];
        Random b=new Random();
        for(int j=0;j<outcomeDimension;j++)
        {
            for(int k=0;k<=lastInnerLayerDim;k++)
            {
                wMatrix[j][k]=(b.nextGaussian()*0.01 );
            }
        }
        this.finalLayer=new Layer(lastInnerLayerDim,outcomeDimension,new RealMatrix(wMatrix));

    }

    ArrayList<Double> predict(ArrayList<Double> x)
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
}

class NetworkComparator implements Comparator<Network>
{
    @Override
    public int compare(Network o1, Network o2)
    {
        return -Double.compare(o1.getFitness(),o2.getFitness());
    }
}
