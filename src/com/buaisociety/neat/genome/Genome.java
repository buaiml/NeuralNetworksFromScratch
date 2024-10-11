package com.buaisociety.neat.genome;

import com.buaisociety.neat.Neat;
import com.buaisociety.neat.calculator.Calculator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents the "gene side" of a neural network. This includes {@link NodeGene neurons}
 * and {@link ConnectionGene connections} between those neurons.
 *
 * <p>This class is separate from the {@link Calculator} class, which is the "phenotype"
 * (the actual neural network, which has behavior). This class is only the "genotype"
 * (the genetic information that describes the neural network). Interestingly, we can
 * use the genotype to evolve.
 */
public class Genome implements Cloneable {

    private static final Mutation[] MUTATIONS = new Mutation[] {
        new MutateAddConnection(),
        new MutateAddNode(),
        new MutateWeights(),
        new MutateBiases(),
    };

    private final Neat neat;
    private List<NodeGene> nodeGenes;
    private List<ConnectionGene> connectionGenes;

    /**
     * Creates an empty Genome, with no nodes or connections.
     */
    public Genome(Neat neat) {
        this.neat = neat;

        // Is using an ArrayList the best choice here?
        // Could we optimize the .contains() calls somehow?
        nodeGenes = new ArrayList<>();
        connectionGenes = new ArrayList<>();
    }

    public Neat getNeat() {
        return neat;
    }

    public List<NodeGene> getNodeGenes() {
        return nodeGenes;
    }

    public void addNodeGene(NodeGene node) {
        if (this.nodeGenes.contains(node)) {
            throw new IllegalArgumentException("Cannot add duplicate node: " + node);
        }

        this.nodeGenes.add(node);
        this.nodeGenes.sort(Comparator.comparingInt(NodeGene::getId));
    }

    public List<ConnectionGene> getConnectionGenes() {
        return connectionGenes;
    }

    public void addConnectionGene(ConnectionGene connection) {
        if (this.connectionGenes.contains(connection)) {
            throw new IllegalArgumentException("Cannot add duplicate connection: " + connection);
        }

        this.connectionGenes.add(connection);
        this.connectionGenes.sort(Comparator.comparingInt(ConnectionGene::getId));
    }

    public void mutate() {
        for (Mutation mutation : MUTATIONS) {
            mutation.mutate(this);
        }
    }

    @Override
    public Genome clone() {
        try {
            Genome clone = (Genome) super.clone();
            clone.nodeGenes = new ArrayList<>();
            clone.nodeGenes.addAll(this.nodeGenes);
            clone.connectionGenes = new ArrayList<>();
            clone.connectionGenes.addAll(this.connectionGenes);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Calculates the compatibility distance between 2 genomes.
     *
     * @param a The first genome.
     * @param b The second genome.
     * @return The compatibility distance.
     */
    public static double distance(Genome a, Genome b) {
        // The number of connections that exist in exactly 1 (not both) genome.
        // These connections are "disjoint" connections.
        int disjoint = 0;

        // The number of connections that exist in both genomes.
        int similar = 0;

        // The total weight difference between connections that exist in both genomes.
        double weightDiff = 0.0;

        // We want to iterate over both genomes simultaneously, and compare
        // the connections. When we find a disjoint connection, we want to
        // "pause" the other iterator, so the disjoint iterator can "catch up"
        int indexA = 0;
        int indexB = 0;
        while (indexA < a.getConnectionGenes().size() && indexB < b.getConnectionGenes().size()) {
            ConnectionGene connectionA = a.getConnectionGenes().get(indexA);
            ConnectionGene connectionB = b.getConnectionGenes().get(indexB);

            // Same connection! :)
            if (connectionA.getId() == connectionB.getId()) {
                similar++;
                weightDiff += Math.abs(connectionA.getWeight() - connectionB.getWeight());
                indexA++;
                indexB++;
            }

            // Connection A is missing in B
            else if (connectionA.getId() < connectionB.getId()) {
                disjoint++;
                indexA++;
            }

            // Connection B is missing in A
            else {
                disjoint++;
                indexB++;
            }
        }

        // The remaining connections are disjoint
        int excess = a.getConnectionGenes().size() - indexA + b.getConnectionGenes().size() - indexB;

        // Normalize the weight difference
        weightDiff /= Math.max(1, similar);

        int n = Math.max(a.getConnectionGenes().size(), b.getConnectionGenes().size());
        if (n < 20) {
            // Smaller genomes are penalized more for excess connections
            n = 1;
        }

        // The compatibility distance formula
        // TODO these probably shouldn't be constant
        double c1 = 1.0;
        double c2 = 1.0;
        double c3 = 0.4;
        return c1 * excess / n + c2 * disjoint / n + c3 * weightDiff;
    }

    /**
     * Crosses over 2 genomes to create a new child genome.
     *
     * <p>This method assumes that the 2 genomes are compatible, i.e. they
     * have similar structure and are likely to produce viable offspring.
     *
     * @param a The first parent genome.
     * @param b The second parent genome.
     * @return The child genome.
     */
    public static Genome crossOver(Genome a, Genome b) {
        Neat neat = a.getNeat();

        Genome child = neat.newGenome(true);

        // We want to iterate over both genomes simultaneously, and compare
        int indexA = 0;
        int indexB = 0;
        while (indexA < a.getConnectionGenes().size() && indexB < b.getConnectionGenes().size()) {
            ConnectionGene connectionA = a.getConnectionGenes().get(indexA);
            ConnectionGene connectionB = b.getConnectionGenes().get(indexB);

            if (connectionA.getId() == connectionB.getId()) {
                // Randomly choose a parent to inherit the connection from
                if (neat.getRandom().nextBoolean()) {
                    addConnection(child, connectionA);
                } else {
                    addConnection(child, connectionB);
                }

                indexA++;
                indexB++;
            } else if (connectionA.getId() < connectionB.getId()) {
                addConnection(child, connectionA);
                indexA++;
            } else {
                addConnection(child, connectionB);
                indexB++;
            }
        }

        // Add the remaining connections
        while (indexA < a.getConnectionGenes().size()) {
            addConnection(child, a.getConnectionGenes().get(indexA));
            indexA++;
        }

        while (indexB < b.getConnectionGenes().size()) {
            addConnection(child, b.getConnectionGenes().get(indexB));
            indexB++;
        }

        return child;
    }

    private static void addConnection(Genome child, ConnectionGene connection) {
        ConnectionGene copy = connection.clone();
        child.addConnectionGene(copy);

        // Add the neurons if they weren't there before
        if (!child.getNodeGenes().contains(copy.getFrom())) {
            child.addNodeGene(copy.getFrom());
        }
        if (!child.getNodeGenes().contains(copy.getTo())) {
            child.addNodeGene(copy.getTo());
        }
    }
}
