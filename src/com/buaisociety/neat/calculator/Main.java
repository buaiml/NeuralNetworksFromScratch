package com.buaisociety.neat.calculator;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple test case to check if we can solve XOR using our custom neural
 * network implementation.
 */
public class Main {

    public static void main(String[] args) {
        List<Node> inputNodes = new ArrayList<>();
        List<Node> hiddenNodes = new ArrayList<>();
        List<Node> outputNodes = new ArrayList<>();

        // Step 1: Create all the nodes we'll use to solve XOR
        Node inputA = new Node(0.0);
        Node inputB = new Node(0.0);
        inputNodes.add(inputA);
        inputNodes.add(inputB);

        Node hiddenC = new Node(0.5);
        hiddenNodes.add(hiddenC);

        Node outputD = new Node(1.0);
        outputNodes.add(outputD);

        // Step 2: Create connections directly between inputs and outputs
        inputA.connect(outputD).setWeight(1.0);
        inputB.connect(outputD).setWeight(1.0);

        // Step 3: Fully connect the hidden node
        inputA.connect(hiddenC).setWeight(1.0);
        inputB.connect(hiddenC).setWeight(1.0);
        hiddenC.connect(outputD).setWeight(-2.0);

        // Step 4: Add a bias value to the hidden node
        hiddenC.setBias(-1.0);

        // Step 5: Actually do the calculations
        Calculator calculator = new Calculator(inputNodes, hiddenNodes, outputNodes);

        double[] inputValues = {1.0, 1.0};
        double[] outputValues = calculator.predict(inputValues);
        System.out.println("Output: " + outputValues[0]);
    }
}
