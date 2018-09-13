package solver;

/**
 * A generic search tree node representing a path through a state graph. Stores
 * a state, the cost of the last edge followed, and the total cost of all edges
 * in the path associated with this node.
 */
public class SearchTreeNode implements Comparable<SearchTreeNode> {

    private SearchTreeNode parent;
    private StateCostPair stateCostPair;
    private double pathCost = 0;
    private int depth = 0;

    //TODO: A HELL LOT NEED TO BE DONE HERE

    public SearchTreeNode(StateCostPair stateCostPair) {
        this.parent = null;
        this.stateCostPair = stateCostPair;
    }

    public SearchTreeNode(SearchTreeNode parent, StateCostPair stateCostPair) {
        this.parent = parent;
        this.stateCostPair = stateCostPair;
        this.pathCost = parent.getPathCost() + stateCostPair.getCost();
        this.depth = parent.getDepth() + 1;
    }

    public SearchTreeNode getParent() {
        return parent;
    }

    public StateCostPair getStateCostPair() {
        return stateCostPair;
    }

    public double getPathCost() {
        return pathCost;
    }

    public int getDepth() {
        return depth;
    }


    /**
     * Compares the path cost of this node to the path cost of node s.
     *
     * Defining a "compareTo" method is necessary in order for searchTreeNodes
     * to be used in a priority queue.
     *
     * @param s node to compare path cost with
     * @return  -1 if this node has a lower total path cost than node s
     *          0 if this node has the same total path cost as node s
     *          1 if this node has a greater total path cost than node s
     */
    public int compareTo(SearchTreeNode s) {
        return Double.compare(this.getPathCost(), s.getPathCost());
    }
}

