package solver;

import java.util.HashSet;
import java.util.Set;

/**
 * A graph data structure that stores nodes of state
 */
public class StateGraph {
    private StateGraphVertex root;
    private StateGraphVertex goal;
    private Set<StateGraphVertex> vertices;
    private Set<StateGraphEdge> edges;

    /**
     * Construct the state graph with root and goal vertices/states
     * @param root
     * @param goal
     */
    public StateGraph(StateGraphVertex root, StateGraphVertex goal) {
        this.root = root;
        this.goal = goal;
        vertices = new HashSet<StateGraphVertex>();
        edges = new HashSet<StateGraphEdge>();
    }

    //TODO: THIS CLASS NEEDS TO BE COMPLETED

}
