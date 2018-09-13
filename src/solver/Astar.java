package solver;

import problem.RobotConfig;

import java.util.*;

/**
 * Astart search algorithm for searching shortest path in the state graph.
 */
public class Astar  {

    private PriorityQueue<Vertex<State>> container;
    private int totNodes = 0;
    private StateGraph<RobotConfig> graph;
    private HashSet<Vertex<State>> visitedNodes = new HashSet<>();
    private HashSet<State> unexploredNodes = new HashSet<>();
    private List<State> path = new ArrayList<>();

    public Astar() {
        container = new PriorityQueue<>();
        this.graph = graph;
    }

    /**
     * If we're given a graph, use the secondly implemented search
     * @param graph the state graph to A* search in
     * @return the optimal path
     */
    public List<State> search(StateGraph<State> graph) {
        return search(graph.getRootVertex(), graph.getGoalVertex());
    }

    public List<State> search(Vertex<State> initial, Vertex<State> goal) {
        // null for now
        Vertex<State> rootNode = new Vertex<>(initial.getState());

        // initialisations
        unexploredNodes.add(rootNode.getState());
        container.add(rootNode);


        while (container.size() > 0) {
            // select the search tree node with the lowest total path cost
            Vertex<State> currentNode = container.poll();

            if (currentNode == null) {
                // no possible path
                return new ArrayList<>();
            }

            // aStarNode.node is currentNode
            AStarNode<State> aStarNode = new AStarNode<>(currentNode);

            totNodes--;

            if (aStarNode.node.getState().equals(goal.getState())) {

                // the path has been found! Calculate the distance
                List<State> pathToGoal = findPathFromNode(aStarNode);

                reset();
                return pathToGoal;
            }

            // not the goal - add all successors to container
            visitedNodes.add(aStarNode.node);
            for (Edge node : aStarNode.node.getNeighbors()) {
                totNodes++;

                if (!visitedNodes.contains(aStarNode.node)) {
                    // if this node hasn't bene visited, add it to the container
                    container.add(aStarNode.node);
                }
            }
        }

        // no solution
        reset();
        return null;
    }

    private List<State> findPathFromNode(AStarNode<State> node) {
        List<State> finalPath = new ArrayList<>();
        // add this node as the first element
        finalPath.add(node.node.getState());
        AStarNode<State> parentNode = node.parent;
        while (parentNode != null) {
            finalPath.add(parentNode.node.getState());
            parentNode = parentNode.parent;
        }

        // reverse this list so that we get the parent -> child path
        Collections.reverse(finalPath);
        return finalPath;
    }

    public void reset() {
        container.clear();
    }

    public int totNodes() {
        return totNodes;
    }

    private class AStarNode<T> {
        Vertex<T> node;
        AStarNode<T> parent;

        AStarNode(Vertex<T> node) {
            this.node = node;
        }

        void setParent(AStarNode<T> parent) {
            this.parent = parent;
        }

    }
}
