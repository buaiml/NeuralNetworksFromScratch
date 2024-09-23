import java.util.Comparator;
import java.util.List;

public class Calculator {

    private List<Node> inputNodes;
    private List<Node> hiddenNodes;
    private List<Node> outputNodes;

    public Calculator(List<Node> inputNodes, List<Node> hiddenNodes, List<Node> outputNodes) {
        this.inputNodes = inputNodes;
        this.hiddenNodes = hiddenNodes;
        this.outputNodes = outputNodes;

        // Sort left -> right
        hiddenNodes.sort(Comparator.comparingDouble(Node::getX));
    }

    public double[] predict(double[] inputValues) {

        // Step 1: Fill in the values for the inputs
        for (int i = 0; i < inputValues.length; i++) {
            inputNodes.get(i).setValue(inputValues[i]);
        }

        // Step 2: Predictions
        for (Node hidden : hiddenNodes) {
            hidden.predict();
        }

        // Step 3: Return our outputs
        double[] outputValues = new double[outputNodes.size()];
        for (int i = 0; i < outputValues.length; i++) {
            outputNodes.get(i).predict();
            outputValues[i] = outputNodes.get(i).getValue();
        }

        return outputValues;
    }
}
