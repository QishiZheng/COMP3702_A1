package solver;

import problem.RobotConfig;

import java.util.*;

/**
 * Astart search algorithm for searching shortest path in the state graph.
 */
public class Astar<T>  {

    private PriorityQueue<Vertex<T>> container = new PriorityQueue<>();
    private int totNodes = 0;
    private StateGraph<RobotConfig> graph;

    public Astar(StateGraph<RobotConfig> graph) {
        this.graph = graph;
    }

    /**
     * If we're given a graph, use the secondly implemented search
     * @param graph the state graph to A* search in
     * @return the optimal path
     */
    public List<T> search(StateGraph<T> graph) {
        return search(graph.getRootVertex(), graph.getGoalVertex());
    }

    public List<T> search(Vertex<T> initial, Vertex<T> goal) {
        // null for now
        Vertex<T> rootNode = new Vertex<>(initial.getState());
        // what will be assigned as later nodes' parents
        AStarNode<T> lastNode = null;

        // initialisations
        container.add(rootNode);
        List<AStarNode<T>> visitedNodes = new ArrayList<>();


        while (container.size() > 0) {
            // select the search tree node with the lowest total path cost
            Vertex<T> currentNode = container.poll();

            if (currentNode == null) {
                // no possible path
                return new ArrayList<>();
            }

            // aStarNode.node is currentNode
            AStarNode<T> aStarNode = new AStarNode<>(currentNode);

            totNodes--;

            if (aStarNode.node.getState().equals(goal.getState())) {

                // the path has been found! Calculate the distance
                List<T> pathToGoal = findPathFromNode(aStarNode);

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

    private List<T> findPathFromNode(AStarNode<T> node) {
        List<T> finalPath = new ArrayList<>();
        // add this node as the first element
        finalPath.add(node.node.getState());
        AStarNode<T> parentNode = node.parent;
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
