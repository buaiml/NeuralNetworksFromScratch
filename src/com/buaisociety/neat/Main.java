package com.buaisociety.neat;

import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {
        int trials = 100;
        int sum = 0;
        for (int i = 0; i < trials; i++) {
            int generations = runTest();
            sum += generations;
        }

        System.out.println("Average generations: " + sum / trials);
    }

    public static int runTest() {
        // We're still trying to solve XOR, but this time using NEAT.
        // 2 inputs, 1 output, 100 population size.
        Neat neat = new Neat(2, 1, 100);
        neat.getRandom().setSeed(ThreadLocalRandom.current().nextLong());

        int generations = 0;
        while (true) {
            // Evaluate each client
            for (Client client : neat.getClients()) {
                updateScore(client);
            }

            if (generations % 100 == 0) {
                System.out.println("Generation " + generations);
                System.out.println("Average score: " + neat.getClients().stream().mapToDouble(Client::getScore).average().getAsDouble());
                System.out.println("Best score: " + neat.getClients().getLast().getScore());
            }

            // Check if any client has solved the XOR problem
            for (Client client : neat.getClients()) {
                if (isPassed(client)) {
                    System.out.println("Solved XOR in " + generations + " generations!");
                    return generations;
                }
            }

            // Evolve the population
            neat.evolve();
            generations++;
        }
    }

    /**
     * Checks if the XOR problem has been solved by the given client.
     */
    public static boolean isPassed(Client client) {
        double[][] inputs = {
            {0.0, 0.0},
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
        };

        double[][] expectedOutputs = {
            {0.0},
            {1.0},
            {1.0},
            {0.0}
        };

        for (int i = 0; i < inputs.length; i++) {
            double[] output = client.getCalculator().predict(inputs[i]);
            double rounded = Math.round(output[0]);
            if (Math.abs(rounded - expectedOutputs[i][0]) > 0.1) {
                return false;
            }
        }

        return true;
    }

    /**
     * Updates the given client's score based on how well it performs on the XOR
     * problem.
     */
    public static void updateScore(Client client) {
        double[][] inputs = {
            {0.0, 0.0},
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
        };

        double[][] expectedOutputs = {
            {0.0},
            {1.0},
            {1.0},
            {0.0}
        };

        // Start with a score of 4.0, since that is the maximum value of the
        // loss function. That way, the maximum loss function gives a score of 0.
        double score = 4.0;
        for (int i = 0; i < inputs.length; i++) {
            double[] output = client.getCalculator().predict(inputs[i]);
            double diff = output[0] - expectedOutputs[i][0];
            score -= diff * diff;
        }

        client.setScore(score);
    }
}
