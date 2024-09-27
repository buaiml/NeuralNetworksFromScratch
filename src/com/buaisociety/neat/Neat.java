package com.buaisociety.neat;

import com.buaisociety.neat.calculator.Node;
import com.buaisociety.neat.genome.ConnectionGene;
import com.buaisociety.neat.genome.Genome;
import com.buaisociety.neat.genome.NodeGene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * The manging class for the NEAT algorithm. This class is responsible for
 * evolving the population of clients, and managing the nodes and connections
 * that make up the neural networks.
 *
 * <p>NEAT stands for "NeuroEvolution of Augmenting Topologies". This algorithm
 * is a method for evolving artificial neural networks with a genetic algorithm.
 *
 * <p>Nodes and connections need to be managed because they should be shared
 * between clients. For example, if you have a neural network with 1 input and
 * 1 output, those 2 nodes should be shared between all clients. Similarly, if
 * you have a connection between those 2 nodes, that connection should also be
 * shared between all clients. This sharing is crucial in genetic evolution, as
 * it lets us compare the "genetics" of different genomes. Matching nodes and
 * connections implies genetic similarity.
 */
public class Neat {

    private int numInputNodes;
    private int numOutputNodes;

    private Random random;
    private List<NodeGene> nodes = new ArrayList<>();
    private Map<ConnectionGene, ConnectionGene> connections = new HashMap<>();
    private Map<ConnectionGene, Integer> replacementNodes = new HashMap<>();
    private List<Client> clients = new ArrayList<>();

    public Neat(int numInputNodes, int numOutputNodes, int numClients) {
        this.random = new Random(1111);
        this.numInputNodes = numInputNodes;
        this.numOutputNodes = numOutputNodes;

        // Instantiating the input nodes
        for (int i = 0; i < numInputNodes; i++) {
            NodeGene inputNode = newNode();
            inputNode.setX(0.1);  // not exactly 0, adds visual padding if visualized
            inputNode.setY((i + 1) / (numInputNodes + 1.0));
        }

        // Instantiating the output nodes
        for (int i = 0; i < numOutputNodes; i++) {
            NodeGene outputNode = newNode();
            outputNode.setX(0.9);  // not exactly 1, adds visual padding if visualized
            outputNode.setY((i + 1) / (numOutputNodes + 1.0));
        }

        // Instantiating the clients
        for (int i = 0; i < numClients; i++) {
            Client client = new Client(this, i);
            clients.add(client);
        }
    }

    public Random getRandom() {
        return random;
    }

    public int getNumInputNodes() {
        return numInputNodes;
    }

    public int getNumOutputNodes() {
        return numOutputNodes;
    }

    public List<Client> getClients() {
        return clients;
    }

    /**
     * Creates a new node and adds it to this manager.
     *
     * @return The new node.
     */
    public NodeGene newNode() {
        int id = nodes.size();
        NodeGene node = new NodeGene(this, id);
        nodes.add(node);
        return node;
    }

    /**
     * Creates a new connection between the 2 given nodes, and adds it to this manager.
     *
     * @param from The node where the connection starts.
     * @param to The node where the connection ends.
     * @return The new connection.
     */
    public ConnectionGene newConnection(NodeGene from, NodeGene to) {
        ConnectionGene connection = new ConnectionGene(this, -1, from, to);

        // See if a connection between the 2 given nodes have ever been created. If
        // so, re-use that connection so the ids match up. Otherwise, create a new
        // connection.
        connection = connections.get(connection);
        if (connection == null) {
            connection = new ConnectionGene(this, connections.size(), from, to);
            connections.put(connection, connection);
        }

        // Clone the connection to avoid genomes modifying the original connection's values.
        return connection.clone();
    }

    public NodeGene newReplacementConnection(ConnectionGene connection) {
        if (replacementNodes.containsKey(connection)) {
            int id = replacementNodes.get(connection);
            return nodes.get(id);
        }

        NodeGene node = newNode();
        replacementNodes.put(connection, node.getId());
        return node;
    }

    public Genome newGenome() {
        Genome genome = new Genome(this);
        for (int i = 0; i < numInputNodes + numOutputNodes; i++) {
            // The nodes already exist, since the input and output cached nodes
            // are created 1 time in the constructor.
            NodeGene node = this.nodes.get(i);
            genome.addNodeGene(node);
        }

        // Fully connect the input nodes to the output nodes
        for (int i = 0; i < numInputNodes; i++) {
            for (int j = numInputNodes; j < numInputNodes + numOutputNodes; j++) {
                NodeGene from = this.nodes.get(i);
                NodeGene to = this.nodes.get(j);
                ConnectionGene connection = new ConnectionGene(this, connections.size(), from, to);
                genome.addConnectionGene(connection);
            }
        }

        return genome;
    }

    public void evolve() {
        // TODO: Kill off the top half of the population
        // TODO: For each dead client, create a new client with a mutated genome
    }

}
