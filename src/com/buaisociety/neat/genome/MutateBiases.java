package com.buaisociety.neat.genome;

import java.util.Random;

/**
 * Loops through every neuron in a network and changes the bias value.
 */
public class MutateBiases implements Mutation {

    @Override
    public void mutate(Genome genome) {
        Random rand = genome.getNeat().getRandom();

        for (NodeGene node : genome.getNodeGenes()) {
            // only 20% of the time
            if (rand.nextDouble() > 0.20)
                continue;

            // Either completely randomize, or slightly modify the bias
            if (rand.nextDouble() > 0.80) {
                node.setBias(rand.nextDouble() * 2 - 1);
            } else {
                double shift = rand.nextDouble() * 0.4 - 0.2;
                node.setBias(node.getBias() + shift);
            }

            // Clamp the bias to the range [-5, 5], to prevent extreme values
            if (node.getBias() < -5) {
                node.setBias(-5);
            } else if (node.getBias() > 5) {
                node.setBias(5);
            }
        }
    }
}
