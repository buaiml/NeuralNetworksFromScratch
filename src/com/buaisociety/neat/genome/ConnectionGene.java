package com.buaisociety.neat.genome;

import com.buaisociety.neat.Neat;

import java.util.Objects;

/**
 * Represents an inheritable connection between 2 nodes, flowing from left -> right.
 * This connection has a weight and can be enabled or disabled.
 *
 * <p>The <code>id</code> of a connection is functionally optional, but is useful for
 * sorting connections in a consistent manner in a {@link Genome}. Sorting is useful
 * for comparing genomes and for crossover.
 */
public class ConnectionGene implements Cloneable {

    private Neat neat;
    private int id;
    private NodeGene from;
    private NodeGene to;

    /**
     * Represents the weight, or multiplier, of this connection. A weight of 0
     * means the connection is effectively disabled. A higher magnitude of weight
     * implies a stronger connection between the two nodes.
     */
    private double weight;

    /**
     * Whether this connection is enabled or not. If disabled, this connection
     * will not be used to contribute values in the neurons.
     */
    private boolean enabled = true;

    public ConnectionGene(Neat neat, int id, NodeGene from, NodeGene to) {
        if (from.getX() >= to.getX()) {
            throw new IllegalArgumentException("from.getX() (" + from.getX() + ") must be to the left of to.getX() (" + to.getX() + ")");
        }

        this.neat = neat;
        this.id = id;
        this.from = from;
        this.to = to;
    }

    public Neat getNeat() {
        return neat;
    }

    public int getId() {
        return id;
    }

    public NodeGene getFrom() {
        return from;
    }

    public NodeGene getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionGene that = (ConnectionGene) o;
        return Objects.equals(neat, that.neat) && Objects.equals(from, that.from) && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public ConnectionGene clone() {
        try {
            return (ConnectionGene) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
