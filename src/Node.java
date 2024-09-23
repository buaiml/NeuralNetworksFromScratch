import java.util.ArrayList;
import java.util.List;

public class Node {

    private double value;
    private double bias;
    private double x;

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

    public Connection connect(Node to) {
        Connection connection = new Connection(this, to);
        to.incomingConnections.add(connection);
        return connection;
    }

    public void predict() {
        // Step 1: Sum up
        double sum = bias;
        for (Connection incomingConnection : incomingConnections) {
            double incomingValue = incomingConnection.getFrom().getValue();
            sum += incomingValue * incomingConnection.getWeight();
        }

        // Step 2: Activation
        double activated = Math.max(0, sum);
        setValue(activated);
    }
}
