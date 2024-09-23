import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Node> inputNodes = new ArrayList<>();
        List<Node> hiddenNodes = new ArrayList<>();
        List<Node> outputNodes = new ArrayList<>();

        Node inputA = new Node(0.0);
        Node inputB = new Node(0.0);
        inputNodes.add(inputA);
        inputNodes.add(inputB);

        Node hiddenC = new Node(0.5);
        hiddenNodes.add(hiddenC);

        Node outputD = new Node(1.0);
        outputNodes.add(outputD);

        // Direct connections
        inputA.connect(outputD).setWeight(1.0);
        inputB.connect(outputD).setWeight(1.0);

        // Hidden connections
        inputA.connect(hiddenC).setWeight(1.0);
        inputB.connect(hiddenC).setWeight(1.0);
        hiddenC.connect(outputD).setWeight(-2.0);

        // Bias
        hiddenC.setBias(-1.0);

        // Actually do the calculations
        Calculator calculator = new Calculator(inputNodes, hiddenNodes, outputNodes);

        double[] inputValues = {1.0, 1.0};
        double[] outputValues = calculator.predict(inputValues);

        System.out.println("Output: " + outputValues[0]);
    }
}
