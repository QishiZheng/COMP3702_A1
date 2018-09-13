package solver;

import java.util.*;

/**
 * Astart search algorithm for searching shortest path in the state graph.
 */
public class Astar implements Agent {

    private PriorityQueue<SearchTreeNode> container;
    private int totNodes = 0;

    public Astar() {
        container = new PriorityQueue<>();
    }

    @Override
    public List<StateCostPair> search(State initial, State goal) {
        container.add(new SearchTreeNode(new StateCostPair(initial, 0)));
        while (container.size() > 0) {
            // select the search tree node with the lowest total path cost
            SearchTreeNode currentNode = container.poll();
            totNodes--;
            State currentState = null;

            if (currentState.equals(goal)) {
                List<StateCostPair> pathToGoal = new LinkedList<StateCostPair>();


                while (currentNode.getParent() != null) {
                    pathToGoal.add(currentNode.getStateCostPair());
                    currentNode = currentNode.getParent();
                }
                Collections.reverse(pathToGoal);
                reset();
                return pathToGoal;
            }

            // not the goal - add all successors to container
            List<StateCostPair> successors = currentState.getBoxes();
        }
        return new ArrayList<>();
    }

    public void reset() {
        container.clear();
    }
}
