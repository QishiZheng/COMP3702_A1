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
    private Vertex<RobotConfig> initial;
    private Vertex<RobotConfig> goal;
    private HashSet<SearchTreeNode> visitedNodes = new HashSet<>();
    private HashSet<State> unexploredNodes = new HashSet<>();
    private List<State> path = new ArrayList<>();


    public Astar() {
        container = new PriorityQueue<>();
        this.initial = graph.getRootVertex();
        this.goal = graph.getGoalVertex();
    }

    /**
     * If we're given a graph, use the secondly implemented search
     * @param graph the state graph to A* search in
     * @return the optimal path
     */
    public List<State> search(StateGraph<RobotConfig> graph) {
        return search(graph.getRootVertex(), graph.getGoalVertex());
    }

    public List<State> search(Vertex<RobotConfig> initial, Vertex<RobotConfig> goal) {
        // null for now
        Vertex<State> rootNode = new Vertex<State>(null);

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

            totNodes--;

            if (currentNode.getState().equals(goal.getState())) {
                // the path has been found! Calculate the distance
                List<State> pathToGoal = new ArrayList<>();
                pathToGoal = findPathFromNode(goal.);


                while (graph != null) {
                    //pathToGoal.add(currentNode.getStateCostPair());
                    pathToGoal.add(currentNode.)
                    currentNode = currentNode.getParent();
                }
                Collections.reverse(pathToGoal);
                reset();
                return pathToGoal;
            }

            // not the goal - add all successors to container
        }
        return new ArrayList<>();
    }

    private List<State> findPathFromNode(Vertex<State> node) {
        List<State> finalPath = new ArrayList<>();
        // add this node as the first element
        finalPath.add(node.getState());
        Vertex<State> parentNode = node.getParent();
        while (parentNode != null) {
            finalPath.add(parentNode.getState());
            parentNode = parentNode.getParent();
        }

        // reverse this list so that we get the parent -> child path
        Collections.reverse(finalPath);
        return finalPath;
    }

    public void reset() {
        container.clear();
    }
}
