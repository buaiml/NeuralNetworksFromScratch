package com.buaisociety.neat.calculator;

/**
 * Represents a connection between 2 nodes.
 */
public class Connection {

    private Node from;
    private Node to;

    /**
     * How "strong" this connection is. Can be positive or negative.
     */
    private double weight;

    public Connection(Node from, Node to) {
        this.from = from;
        this.to = to;
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
