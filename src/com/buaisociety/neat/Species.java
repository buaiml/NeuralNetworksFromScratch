package com.buaisociety.neat;

import com.buaisociety.neat.genome.Genome;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A species is a group of similar clients (group of similar genomes). The
 * similarity is determined by the compatibility distance between genomes.
 *
 * <p>Species are used to group clients together so that they can be compared
 * and evolved together. This is important because it allows us to compare the
 * "genetics" of different clients. Matching genomes implies genetic similarity.
 */
public class Species {

    private Neat neat;
    private int id;
    private Client base;

    private List<Client> clients = new ArrayList<>();
    private double score;
    private int generations = 0;
    private boolean isExtinct = false;

    public Species(Neat neat, int id, Client base) {
        this.neat = neat;
        this.id = id;
        this.base = base;

        // start with 1 member in our species
        this.clients.add(base);
    }

    public Neat getNeat() {
        return neat;
    }

    public int getId() {
        return id;
    }

    public Client getBase() {
        return base;
    }

    public List<Client> getClients() {
        return clients;
    }

    public double getScore() {
        return score;
    }

    public int getGenerations() {
        return generations;
    }

    public boolean isExtinct() {
        return isExtinct;
    }

    /**
     * Returns a random client from this species, or null if the species is
     * empty (extinct).
     *
     * @return a random client from this species
     */
    public Client getRandom() {
        if (clients.isEmpty()) {
            return null;
        }

        return clients.get(neat.getRandom().nextInt(clients.size()));
    }

    /**
     * Returns true if the given client can be added to this species.
     *
     * <p>A client can be added to this species if the genome is similar to the
     * genome of the base client of this species.
     *
     * @param client The client to check to add.
     * @return true if the client can be added to this species
     */
    public boolean matches(Client client) {
        double distance = Genome.distance(base.getGenome(), client.getGenome());
        return distance < 2.0;  // TODO: This definitely shouldn't be a constant
    }

    /**
     * Adds the given client to this species if it matches the base client.
     *
     * <p>If the client does not match the base client, it will not be added to
     * the species.
     *
     * @param client The client to add.
     * @return true if the client was added to the species
     */
    public boolean add(Client client, boolean force) {
        if (isExtinct) {
            throw new IllegalStateException("Cannot add to an extinct species");
        }

        if (force || matches(client)) {
            client.setSpecies(this);
            clients.add(client);
            return true;
        }

        // Client did not match, so we cannot add it to the species
        return false;
    }

    public void evaluate() {
        // reset the old score
        score = 0.0;

        for (Client client : clients) {
            score += client.getScore();
        }

        score /= clients.size();
        score = Math.max(0.0001, score);  // avoid division by zero
        generations++;
    }

    public void reset() {
        score = 0.0;
        Client newBase = getRandom();
        if (newBase != null) {
            base = newBase;
        }

        for (Client client : clients) {
            client.setSpecies(null);
        }
        clients.clear();

        // Force add the base client back into the species
        add(base, true);
    }

    /**
     * Marks this species as extinct, and removes all clients from the species.
     */
    public void goExtinct() {
        isExtinct = true;
        for (Client client : clients) {
            client.setSpecies(null);
        }
        clients.clear();
    }

    public void kill(double percentage) {

        // Only kill off older species that have yet to innovate
        int gracePeriod = 10; // TODO: This shouldn't be a constant
        if (generations < gracePeriod) {
            return;
        }

        // Kill off the worst performers by sorting by client score
        clients.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        int killCount = (int) (percentage * clients.size());
        for (int i = 0; i < killCount; i++) {
            clients.get(i).setSpecies(null);
            clients.remove(0);
        }

        // If we removed the base client, we should select a new base client
        if (!clients.contains(base)) {
            if (clients.isEmpty()) {
                goExtinct();
            } else {
                base = getRandom();
            }
        }
    }

    /**
     * Breeds 2 random clients from this species to create a new genome.
     *
     * @return The new genome created by breeding 2 random clients from this species.
     */
    public Genome breed() {
        Client a = getRandom();
        Client b = getRandom();

        // Empty species cannot breed
        if (a == null || b == null) {
            return null;
        }

        // Try to choose the best client to be first, since we bias towards the
        // first genome in the crossover method sometimes.
        if (a.getScore() > b.getScore()) {
            return Genome.crossOver(a.getGenome(), b.getGenome());
        } else {
            return Genome.crossOver(b.getGenome(), a.getGenome());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Species species = (Species) o;
        return id == species.id && Objects.equals(neat, species.neat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(neat, id);
    }

    @Override
    public String toString() {
        return "Species{" +
            "base=" + base +
            ", score=" + score +
            ", isExtinct=" + isExtinct +
            '}';
    }
}
