package com.buaisociety.neat.genome;

/**
 * Represents a random change that may be applied to a {@link Genome}.
 */
public interface Mutation {

    /**
     * Applies this mutation to the given {@link Genome}.
     *
     * @param genome The genome to apply the mutation to.
     */
    void mutate(Genome genome);
}
