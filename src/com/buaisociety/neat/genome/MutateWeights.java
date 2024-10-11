package com.buaisociety.neat.genome;

import java.util.Random;

public class MutateWeights implements Mutation {

    @Override
    public void mutate(Genome genome) {
        Random rand = genome.getNeat().getRandom();

        for (ConnectionGene connection : genome.getConnectionGenes()) {
            if (rand.nextDouble() < 0.80) {
                double shift = rand.nextDouble(0.4) - 0.2;
                connection.setWeight(connection.getWeight() + shift);
            } else {
                connection.setWeight(rand.nextDouble(2.0) - 1.0);
            }
        }
    }
}
