package com.buaisociety.neat.genome;

import java.util.Random;

/**
 * This mutation randomly selects 2 nodes, and tries to make a connection
 * between them.
 */
public class MutateAddConnection implements Mutation {

    @Override
    public void mutate(Genome genome) {
        Random rand = genome.getNeat().getRandom();

        // Only a 5% chance to add a new connection
        if (rand.nextDouble() > 0.05)
            return;

        int attempts = 100;
        while (attempts-- > 0) {
            int randomIndex1 = rand.nextInt(genome.getNodeGenes().size());
            int randomIndex2 = rand.nextInt(genome.getNodeGenes().size());

            NodeGene from = genome.getNodeGenes().get(randomIndex1);
            NodeGene to = genome.getNodeGenes().get(randomIndex2);

            // Swap to make sure connections flow left -> right
            if (from.getX() > to.getX()) {
                NodeGene temp = from;
                from = to;
                to = temp;
            }

            // If the x positions are the same, try again
            if (Double.compare(from.getX(), to.getX()) == 0) {
                continue;
            }

            // If a connection between these 2 nodes already exists, try again
            ConnectionGene connection = genome.getNeat().newConnection(from, to);
            if (genome.getConnectionGenes().contains(connection)) {
                continue;
            }

            // Success! Add the connection and stop
            genome.addConnectionGene(connection);
            break;
        }
    }
}
