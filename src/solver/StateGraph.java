package solver;

import java.util.HashSet;
import java.util.Set;

/**
 * A graph data structure that stores nodes of state
 */
public class StateGraph {
    private Vertex root;
    private Vertex goal;
    private Set<Vertex> vertices;
    private Set<Edge> edges;

    /**
     * Construct the state graph with root and goal vertices/states
     * @param root
     * @param goal
     */
    public StateGraph(Vertex root, Vertex goal) {
        this.root = root;
        this.goal = goal;
        vertices = new HashSet<Vertex>();
        edges = new HashSet<Edge>();
    }

    public StateGraph() {
        vertices = new HashSet<Vertex>();
        edges = new HashSet<Edge>();
    }

    /**
     * Get the root vertex
     *
     * @return the root vertex if one is set, null if no vertex has been set as
     *         the root.
     */
    public Vertex getRootVertex() {
        return this.root;
    }

    /**
     * Get the goal vertex
     *
     * @return the goal vertex if one is set, null if no vertex has been set as
     *         the goal.
     */
    public Vertex getGoalVertex() {
        return this.goal;
    }

    /**
     * Add the given vertex v to this graph
     * @param v the given vertex
     * @return true iff the the given vertex is added to this graph
     */
    public boolean addVertex(Vertex v) {
        if (!this.hasVertex(v)) {
            this.vertices.add(v);
            return true;
        }
        return false;
    }

    /**
     * Remove the given vertex if this graph contains given vertex,
     * and set root vertex to null if given vertex is root vertex
     * @param v given vertex
     * @return true iff the given vertex is removed from this graph
     */
    public boolean removeVertex(Vertex v) {
        if (this.hasVertex(v)) {
            this.vertices.remove(v);
            if (v == this.root) {
                root = null;
            }
            return true;
        }
        return false;
    }


    /**
     * Add the given edge v to this graph and
     * add it also as neighbor to both the start vertex and end vertex of this edge
     * @param e the given edge
     * @return true iff the the given edge is added to this graph
     */
    public boolean addEdge(Edge e) {
        if (!this.hasEdge(e)) {
            this.edges.add(e);
            e.getStart().addNeighbor(e);
            Edge endNeighbor = new Edge(e.getEnd(), e.getStart(), e.getCost());
            e.getEnd().addNeighbor(endNeighbor);
            return true;
        }
        return false;
    }

    /**
     * Remove the given edge e if this graph contains given edge and
     * remove it also from the neighbors of  both the start vertex and end vertex of this edge
     * @param e given edge
     * @return true iff the given edge is removed from this graph
     */
    public boolean removeEdge(Edge e) {
        if (this.hasEdge(e)) {
            this.edges.remove(e);
            e.getStart().removeNeighbor(e);
            Edge endNeighbor = new Edge(e.getEnd(), e.getStart(), e.getCost());
            e.getEnd().addNeighbor(endNeighbor);
            return true;
        }
        return false;
    }

    /**
     * Check if the graph contains given vertex
     * @param v given vertex
     * @return true if this graph contains v
     */
    public boolean hasVertex(Vertex v) { return this.vertices.contains(v); }

    /**
     * Check if the graph contains given edge
     * @param e given edge
     * @return true if this graph contains e
     */
    public boolean hasEdge(Edge e) {
        if ((this.hasVertex(e.getStart())) && (this.hasVertex(e.getEnd()))) {
            return this.edges.contains(e);
        }

        return false;
    }

    /**
     * check if there are any verticies in the graph
     *
     * @return true if there are no verticies in the graph
     */
    public boolean isEmpty() {
        return vertices.size() == 0;
    }

    /**
     *
     * @return the number of vertices in this graph
     */
    public int getNumOfVertex() { return this.vertices.size(); }

}
