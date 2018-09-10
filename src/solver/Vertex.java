package solver;

import java.util.HashSet;
import java.util.Set;

/**
 * Node class for StateGraph
 *
 */
public class Vertex<State> {
    private State state;
    //all edges that connected to this vertex
    private Set<Edge> neighbors;

    public Vertex(State s) {
        this.state = s;
        this.neighbors = new HashSet<Edge>();
    }

    /**
     *
     * @return the state that stored in this node
     */
    public State getState() {
        return this.state;
    }

    /**
     *
     * @return a set of all neighbor edges that this vertex connected to
     */
    public Set getNeighbors() {
        return new HashSet<Edge>(this.neighbors);
    }

    /**
     * Add the given edge e to the neighbors of this vertex
     * @param e given edge
     * @return true iff e has been added to the neighbors of this vertex
     */
    public boolean addNeighbor(Edge e) {
        if(!this.hasNeighbor(e)) {
            this.neighbors.add(e);
            return true;
        }
        return false;
    }

    /**
     * Remove the given edge e from the neighbors of this vertex
     * @param e given edge
     * @return true iff e has been removed from the neighbors of this vertex
     */
    public boolean removeNeighbor(Edge e) {
        if(this.hasNeighbor(e)) {
            this.neighbors.remove(e);
            return true;
        }
        return false;
    }


    /**
     *  check if this edge is a neighbor of this vertex
     * @param e given edge neighbor
     * @return true iff edge e is connected to this vertex
     */
    public boolean hasNeighbor(Edge e) { return this.neighbors.contains(e); }

    /**
     *
     * @return the number of edges that this vertex connected to
     */
    public int getNumOfNeighbors() { return this.neighbors.size(); }

    /**
     * Check if the given object is equal to this Vertex
     * @param obj given object
     * @return true if equal
     */
    public boolean equals(Object obj) {
        if(obj instanceof Vertex) {
           Vertex n = (Vertex) obj;
           if(n.getState().equals(this.state)) {
               return true;
           }
        }
        return false;
    }


}
