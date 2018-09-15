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
     * Construct the state graph with root and goal vertices/states,
     * add root and goal vertices to the graph
     * @param root
     * @param goal
     */
    public StateGraph(Vertex<T> root, Vertex<T> goal) {
        this.root = root;
        this.goal = goal;
        vertices = new ArrayList<Vertex<T>>();
        edges = new ArrayList<Edge<T>>();

        vertices.add(root);
        vertices.add(goal);
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
     *
     * @return a list of all vertices in the graph
     */
    public List<Vertex<T>> getAllVertices() {
        return new ArrayList<Vertex<T>>(this.vertices);
    }

    /**
     * Get given vertex in the graph if exists
     * @param vertex given vertex
     * @return the vertex that is equal to given vertex,
     *          return null if not found
     */
    public Vertex<T> getVertexInGraph(Vertex<T> vertex) {
        //return null if there is no such vertex in the graph
        if(!hasVertex(vertex)) {
            return null;
        } else {
            for(Vertex<T> v : getAllVertices()) {
                if(v.equals(vertex)) { return v;}
            }
        }
        return null;
    }

    /**
     * Get a list of successors of the given node if the given vertex is in the graph
     * @param vertex given vertex
     * @return list of successors
     */
    public List<Vertex<T>> getSuccessors(Vertex<T> vertex) {
        List<Vertex<T>> succs = new LinkedList<>();
        for(Edge<T> e : getVertexInGraph(vertex).getNeighbors()) {
            if(e.getStart().equals(vertex)) {
                succs.add(e.getEnd());
            } else if(e.getEnd().equals(vertex)) {
                succs.add(e.getStart());
            }
        }
        return succs;
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
    public int numOfVertex() { return this.vertices.size(); }


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
