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
}
