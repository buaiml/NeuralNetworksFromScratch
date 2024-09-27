package com.buaisociety.neat.calculator;

import com.buaisociety.neat.genome.ConnectionGene;
import com.buaisociety.neat.genome.Genome;
import com.buaisociety.neat.genome.NodeGene;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wraps all the neurons in a neural network together, so we can pass in an
 * array of data, and get out an array of predicted data.
 */
public class Calculator {

    private List<Node> inputNodes;
    private List<Node> hiddenNodes;
    private List<Node> outputNodes;

    public Calculator(Genome genome) {
        this.inputNodes = new ArrayList<>();
        this.hiddenNodes = new ArrayList<>();
        this.outputNodes = new ArrayList<>();

        Map<Integer, Node> nodeCache = new HashMap<>();
        for (NodeGene nodeGene : genome.getNodeGenes()) {
            Node node = new Node(nodeGene.getX());
            node.setBias(nodeGene.getBias());
            nodeCache.put(nodeGene.getId(), node);

            if (nodeGene.isInput()) {
                inputNodes.add(node);
            } else if (nodeGene.isOutput()) {
                outputNodes.add(node);
            } else {
                hiddenNodes.add(node);
            }
        }

        // Sort left -> right
        hiddenNodes.sort(Comparator.comparingDouble(Node::getX));

        for (ConnectionGene connectionGene : genome.getConnectionGenes()) {
            Node from = nodeCache.get(connectionGene.getFrom().getId());
            Node to = nodeCache.get(connectionGene.getTo().getId());
            Connection connection = from.connect(to);

            connection.setWeight(connectionGene.getWeight());
        }
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
