import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single node, or neuron, in a neural network. This implementation
 * includes a bias value, which is atypical for neural networks.
 */
public class Node {

    private double value;
    private double bias;
    private double x;

    /**
     * The list of all connections that "flow into" this neuron.
     */
    private List<Connection> incomingConnections = new ArrayList<>();

    public Node(double x) {
        this.x = x;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    /**
     * Creates a new connection between <code>this</code> neuron and the
     * target "<code>to</code>" neuron. The connection will start with a weight
     * of <code>0</code>.
     *
     * @param to The target node to connect to (should be to the right of this node).
     * @return The new connection between the nodes.
     */
    public Connection connect(Node to) {
        Connection connection = new Connection(this, to);
        to.incomingConnections.add(connection);
        return connection;
    }

    /**
     * Predicts the value of this neuron based on the values coming in from the
     * previous neurons and their connections to this neuron.
     *
     * <p>Note: Remember to call {@link Node#predict()} on all neurons to the
     * left of this neuron before calling predict here.
     */
    public void predict() {
        // Step 1: Sum up values
        double sum = bias;
        for (Connection incomingConnection : incomingConnections) {
            double incomingValue = incomingConnection.getFrom().getValue();
            sum += incomingValue * incomingConnection.getWeight();
        }

        // Step 2: Activation, to make the result non-linear
        double activated = Math.max(0, sum);
        setValue(activated);
    }
}
