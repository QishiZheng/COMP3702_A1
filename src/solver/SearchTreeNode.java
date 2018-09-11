package solver;

/**
 * A generic search tree node representing a path through a state graph. Stores
 * a state, the cost of the last edge followed, and the total cost of all edges
 * in the path associated with this node.
 *************************************************************************
 * This is for A* search algorithm and maybe RRT
 * **********************************************************************
 */
public class SearchTreeNode implements Comparable<SearchTreeNode> {

    private SearchTreeNode parent;
    private double cost;

    //TODO: A HELL LOT NEED TO BE DONE HERE



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
        return Double.compare(this.cost, s.cost);
    }
}

