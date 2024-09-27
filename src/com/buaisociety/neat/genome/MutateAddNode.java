package com.buaisociety.neat.genome;

import java.util.Random;

public class MutateAddNode implements Mutation {

    @Override
    public void mutate(Genome genome) {
        Random rand = genome.getNeat().getRandom();

        // Only a 5% chance to add a new node
        if (rand.nextDouble() > 0.05)
            return;

        ConnectionGene randomConnection = genome.getConnectionGenes().get(rand.nextInt(genome.getConnectionGenes().size()));
        NodeGene middle = genome.getNeat().newReplacementConnection(randomConnection);

        ConnectionGene a = genome.getNeat().newConnection(randomConnection.getFrom(), middle);
        ConnectionGene b = genome.getNeat().newConnection(middle, randomConnection.getTo());

        a.setWeight(1.0);
        b.setWeight(randomConnection.getWeight());
        randomConnection.setEnabled(false);

        genome.addNodeGene(middle);
        genome.addConnectionGene(a);
        genome.addConnectionGene(b);
    }
}
