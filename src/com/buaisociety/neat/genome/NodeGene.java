package com.buaisociety.neat.genome;

import com.buaisociety.neat.Neat;

import java.util.Objects;

public class NodeGene implements Cloneable {

    private Neat neat;
    private int id;

    private double bias;

    // Just represents the position of this neuron. This is effectively
    private double x;
    private double y;

    public NodeGene(Neat neat, int id) {
        this.neat = neat;
        this.id = id;
    }

    public Neat getNeat() {
        return neat;
    }

    public void setNeat(Neat neat) {
        this.neat = neat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public boolean isInput() {
        return this.id < neat.getNumInputNodes();
    }

    public boolean isOutput() {
        return this.id >= neat.getNumInputNodes() && this.id < neat.getNumInputNodes() + neat.getNumOutputNodes();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeGene nodeGene = (NodeGene) o;
        return id == nodeGene.id && Objects.equals(neat, nodeGene.neat);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "NodeGene{" +
            ", id=" + id +
            ", x=" + x +
            ", y=" + y +
            '}';
    }

    @Override
    public NodeGene clone() {
        try {
            return (NodeGene) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
