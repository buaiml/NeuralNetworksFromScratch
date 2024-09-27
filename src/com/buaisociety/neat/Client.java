package com.buaisociety.neat;

import com.buaisociety.neat.calculator.Calculator;
import com.buaisociety.neat.genome.Genome;

public class Client {

    private Neat neat;
    private int id;

    private Genome genome;
    private Calculator calculator;
    private double score;

    public Client(Neat neat, int id) {
        this.neat = neat;
        this.id = id;


    }


}
