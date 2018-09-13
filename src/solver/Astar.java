package solver;

import problem.RobotConfig;

import java.util.*;

/**
 * Astart search algorithm for searching shortest path in the state graph.
 */
public class Astar  {

    private PriorityQueue<Vertex<State>> container = new PriorityQueue<>();
    private int totNodes = 0;
    private StateGraph<RobotConfig> graph;
    private HashSet<AStarNode<State>> visitedNodes = new HashSet<>();
    private List<State> path = new ArrayList<>();

    public Astar(StateGraph<RobotConfig> graph) {
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
        // what will be assigned as later nodes' parents
        AStarNode<State> lastNode = null;

        // initialisations
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
            visitedNodes.add(aStarNode);
            for (Edge node : aStarNode.node.getNeighbors()) {
                totNodes++;

                if (!visitedNodes.contains(aStarNode)) {
                    // if this node hasn't bene visited, add it to the container
                    aStarNode.parent = lastNode;
                    container.add(aStarNode.node);
                    lastNode = aStarNode;
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
        visitedNodes.clear();
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

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof AStarNode)) {
                return false;
            }
            AStarNode other = (AStarNode) o;
            return node.equals(other.node) && parent.equals(other.parent);
        }
    }
}
