package NeuralNetwork;


import java.util.Objects;

public class Layer
{
    int inputDimension;
    int numberOfNeurons;
    RealMatrix NeuronValues;

    public Layer(int inputDimension, int numberOfNeurons, RealMatrix neuronValues) {
        this.inputDimension = inputDimension;
        this.numberOfNeurons = numberOfNeurons;
        NeuronValues = neuronValues.copy();
    }

    public int getInputDimension() {
        return inputDimension;
    }

    public void setInputDimension(int inputDimension) {
        this.inputDimension = inputDimension;
    }

    public int getNumberOfNeurons() {
        return numberOfNeurons;
    }

    public void setNumberOfNeurons(int numberOfNeurons) {
        this.numberOfNeurons = numberOfNeurons;
    }

    public RealMatrix getNeuronValues() {
        return NeuronValues;
    }

    public void setNeuronValues(RealMatrix neuronValues) {
        NeuronValues = neuronValues;
    }

    public double getNeuronValue(int i, int j)
    {
        return NeuronValues.getEntry(i,j);
    }
    public void setNeuronValue(int i,int j, double value)
    {
        NeuronValues.setEntry(i,j,value);
    }

    public RealMatrix getOutcomes(RealMatrix x)
    {
        return NeuronValues.multiply(x);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Layer)) return false;
        Layer layer = (Layer) o;
        return getInputDimension() == layer.getInputDimension() && getNumberOfNeurons() == layer.getNumberOfNeurons() && Objects.equals(getNeuronValues(), layer.getNeuronValues());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInputDimension(), getNumberOfNeurons(), getNeuronValues());
    }
}
