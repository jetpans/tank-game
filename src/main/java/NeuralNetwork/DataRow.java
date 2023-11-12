package NeuralNetwork;
import java.util.ArrayList;

public class DataRow
{
    ArrayList<Double> features=new ArrayList<>();
    ArrayList<Double> outcomes=new ArrayList<>();

    public DataRow(int outcomeDimension,String line) {
        String[] nums=line.split(",");
        for(int i=0;i<nums.length;i++)
        {
            if(i<nums.length-outcomeDimension)
            {
                features.add(Double.parseDouble(nums[i]));
            }
            else
            {
                outcomes.add(Double.parseDouble(nums[i]));
            }
        }

    }

    public ArrayList<Double> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<Double> features) {
        this.features = features;
    }

    public ArrayList<Double> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(ArrayList<Double> outcomes) {
        this.outcomes = outcomes;
    }
}
