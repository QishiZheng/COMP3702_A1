package solver;

import problem.ProblemSpec;

import java.io.IOException;
import java.util.*;

/**
 * A graph data structure that for building roadmap with PRM
 */
public class StateGraph<T> {
    private Vertex<T> root;
    private Vertex<T> goal;
    private List<Vertex<T>> vertices;
    private List<Edge<T>> edges;

    /**
     * Construct the state graph with root and goal vertices/states
     * @param root
     * @param goal
     */
    public StateGraph(Vertex<T> root, Vertex<T> goal) {
        this.root = root;
        this.goal = goal;
        vertices = new ArrayList<Vertex<T>>();
        edges = new ArrayList<Edge<T>>();
    }

    public StateGraph() {
        vertices = new ArrayList<Vertex<T>>();
        edges = new ArrayList<Edge<T>>();
    }

    /**
     * Get the root vertex
     *
     * @return the root vertex if one is set, null if no vertex has been set as
     *         the root.
     */
    public Vertex<T> getRootVertex() {
        return this.root;
    }

    /**
     * Get the goal vertex
     *
     * @return the goal vertex if one is set, null if no vertex has been set as
     *         the goal.
     */
    public Vertex<T> getGoalVertex() {
        return this.goal;
    }

    /**
     * Add the given vertex v to this graph
     * @param v the given vertex
     * @return true iff the the given vertex is added to this graph
     */
    public boolean addVertex(Vertex<T> v) {
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
    public boolean removeVertex(Vertex<T> v) {
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
    public boolean addEdge(Edge<T> e) {
        if (!this.hasEdge(e)) {
            this.edges.add(e);
            e.getStart().addNeighbor(e);
//            Edge endNeighbor = new Edge(e.getEnd(), e.getStart(), e.getCost());
//            e.getEnd().addNeighbor(endNeighbor);
            e.getEnd().addNeighbor(e);
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
    public boolean removeEdge(Edge<T> e) {
        if (this.hasEdge(e)) {
            this.edges.remove(e);
            e.getStart().removeNeighbor(e);
//            Edge endNeighbor = new Edge(e.getEnd(), e.getStart(), e.getCost());
//            e.getEnd().removeNeighbor(endNeighbor);
            e.getEnd().removeNeighbor(e);
            return true;
        }
        return false;
    }

    /**
     * Check if the graph contains given vertex
     * @param v given vertex
     * @return true if this graph contains v
     */
    public boolean hasVertex(Vertex<T> v) { return this.vertices.contains(v); }

    /**
     * Check if the graph contains given edge and the two vertices in the edge
     * @param e given edge
     * @return true if this graph contains e and the two vertices in the edge
     */
    public boolean hasEdge(Edge<T> e) {
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


//    public static void main(String[] args) {
//        ProblemSpec ps = new ProblemSpec();
//        ProblemSpec ps2 = new ProblemSpec();
//        try {
//            ps.loadProblem("input1.txt");
//            ps2.loadProblem("input2.txt");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        StateGraph sg = new StateGraph();
//        Vertex v1 = new Vertex(ps);
//        Vertex v2 = new Vertex(ps2);
//        sg.addVertex(v1);
//        sg.addVertex(v2);
//        Edge e1 = new Edge(v1, v2);
//        Edge e2 = new Edge(v2, v1);
//        sg.addEdge(e2);
//        System.out.println("Has Edge e1: " + sg.hasEdge(e1));
//        System.out.println("v1 has: " + Arrays.toString(v1.getNeighbors().toArray()));
//        System.out.println(sg.getNumOfVertex());
//    }
}
