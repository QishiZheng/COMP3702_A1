package solver;

import problem.RobotConfig;

import java.util.ArrayList;

import java.util.List;


/**
 * Node/Vertex class for StateGraph
 *
 */
public class Vertex<T> {
    private T state;
    //all edges that connected to this vertex
    private List<Edge> neighbors;

    public Vertex(T s) {
        this.state = s;
        this.neighbors = new ArrayList<Edge>();
    }

    /**
     *
     * @return the state that stored in this node
     */
    public T getState() {
        return this.state;
    }

    /**
     *
     * @return a set of all neighbor edges that this vertex connected to
     */
    public List<Edge> getNeighbors() {
        return new ArrayList<Edge>(this.neighbors);
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
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vertex) {
           Vertex n = (Vertex) obj;
           if(n.getState().equals(this.state)) {
               return true;
           }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.state.hashCode()*32;
    }

    public String toString() {
        if(getState() instanceof RobotConfig) {
            RobotConfig r = (RobotConfig) getState();
            return "(" + r.getPos().getX() + ", "
                    + r.getPos().getY() + ", "
                    + r.getOrientation() + ")";
        }
        return "";
    }
}
