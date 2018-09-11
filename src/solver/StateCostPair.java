package solver;

public class StateCostPair implements Comparable<StateCostPair> {

    private State state;
    private double cost;

    public StateCostPair(State state, double cost) {
        this.state = state;
        this.cost = cost;
    }

    public State getState() {
        return state;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public int compareTo(StateCostPair s) {
        return Double.compare(this.cost, s.getCost());
    }
}
