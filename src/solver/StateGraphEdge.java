package solver;

/**
 * A class for the edge of StateGraph.
 * Each edge contains the start vertex, end vertex, and the cost from start to end
 */
public class StateGraphEdge{
    //start vertex
    private StateGraphVertex start;
    //target vertex
    private StateGraphVertex end;
    //the cost from start to end
    private double cost;

    public StateGraphEdge(StateGraphVertex start, StateGraphVertex end, double cost) {
        this.start = start;
        this.end = end;
        this.cost = cost;
    }

    //set cost to 1 if not cost given
    public StateGraphEdge(StateGraphVertex start, StateGraphVertex end) {
        this.start = start;
        this.end = end;
        this.cost = 1;
    }

    /**
     *
     * @return start vertex of this edge
     */
    public StateGraphVertex getStart() {
        return this.start;
    }

    /**
     *
     * @return end vertex of this edge
     */
    public StateGraphVertex getEnd() {
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
     * Check if the given object is equal to this StateGraphEdge
     * @param obj given object
     * @return true if it is equal
     */
    public boolean equals(Object obj) {
        if(obj instanceof StateGraphEdge) {
            StateGraphEdge edge = (StateGraphEdge) obj;
            if(this.start.equals(edge.getStart())
                    && this.end.equals(((StateGraphEdge) obj).getEnd())) {
                return true;
            }
        }
        return false;
    }
}
