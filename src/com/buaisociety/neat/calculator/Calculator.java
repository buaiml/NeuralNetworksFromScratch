package com.buaisociety.neat.calculator;

import java.util.Comparator;
import java.util.List;

/**
 * Wraps all the neurons in a neural network together, so we can pass in an
 * array of data, and get out an array of predicted data.
 */
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

    /**
     * Inserts the <code>inputValues</code> into the neural network, then
     * predicts all neuron values using {@link Node#predict()}.
     *
     * @param inputValues The array of inputs to feed into the neural network.
     * @return The array of outputs from the output neurons.
     */
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
