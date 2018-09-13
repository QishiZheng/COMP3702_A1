package solver;

/**
 * A class for the edge of StateGraph.
 * Each edge contains the start vertex, end vertex, and the cost from start to end
 */
public class Edge<T> {
    //start vertex
    private Vertex<T> start;
    //target vertex
    private Vertex<T> end;
    //the cost from start to end
    private double cost;

    public Edge(Vertex<T> start, Vertex<T> end, double cost) {
        this.start = start;
        this.end = end;
        this.cost = cost;
    }

    //set cost to 1 if no cost given
    public Edge(Vertex<T> start, Vertex<T> end) {
        this.start = start;
        this.end = end;
        this.cost = 1;
    }

    /**
     *
     * @return start vertex of this edge
     */
    public Vertex<T> getStart() {
        return this.start;
    }

    /**
     *
     * @return end vertex of this edge
     */
    public Vertex<T> getEnd() {
        return this.end;
    }

    /**
     *
     * @return the the cost from start to end
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Check if the given object is equal to this Edge
     * @param obj given object
     * @return true if it is equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Edge) {
            Edge edge = (Edge) obj;
            if((this.start.equals(edge.getStart()) && this.end.equals((edge.getEnd()))) ||
                    (this.start.equals((edge.getEnd())) && this.end.equals(edge.getStart()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.start.hashCode() + this.end.hashCode()) * 32;
    }

    @Override
    public String toString() {
        return "Edge from " + this.start + " to " + this.end + "\n";
    }
}
