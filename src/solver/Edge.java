package solver;

/**
 * A class for the edge of StateGraph.
 * Each edge contains the start vertex, end vertex, and the cost from start to end
 */
public class Edge {
    //start vertex
    private Vertex start;
    //target vertex
    private Vertex end;
    //the cost from start to end
    private double cost;

    public Edge(Vertex start, Vertex end, double cost) {
        this.start = start;
        this.end = end;
        this.cost = cost;
    }

    //set cost to 1 if no cost given
    public Edge(Vertex start, Vertex end) {
        this.start = start;
        this.end = end;
        this.cost = 1;
    }

    /**
     *
     * @return start vertex of this edge
     */
    public Vertex getStart() {
        return this.start;
    }

    /**
     *
     * @return end vertex of this edge
     */
    public Vertex getEnd() {
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
    public boolean equals(Object obj) {
        if(obj instanceof Edge) {
            Edge edge = (Edge) obj;
            if(this.start.equals(edge.getStart())
                    && this.end.equals(((Edge) obj).getEnd())) {
                return true;
            }
        }
        return false;
    }
}
