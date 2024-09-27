package com.buaisociety.neat;

import com.buaisociety.neat.calculator.Calculator;
import com.buaisociety.neat.genome.Genome;

/**
 * Represents 1 client in the NEAT algorithm. A client is basically just a
 * neural network mapped to a score, so we can more easily evolve the
 * neural network.
 */
public class Client {

    private Neat neat;
    private int id;

    private Genome genome;
    private Calculator calculator;
    private double score;

    public Client(Neat neat, int id) {
        this.neat = neat;
        this.id = id;
        this.genome = neat.newGenome();
    }

    public Neat getNeat() {
        return neat;
    }

    public int getId() {
        return id;
    }

    public Genome getGenome() {
        return genome;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
        calculator = null;
    }

    public Calculator getCalculator() {
        if (calculator == null) {
            calculator = new Calculator(genome);
        }

        return calculator;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Mutates the genome stored by this client.
     */
    public void mutate() {
        genome.mutate();
        calculator = null;
    }
}
