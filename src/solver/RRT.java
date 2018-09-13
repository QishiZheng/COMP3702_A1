package solver;

import problem.Box;
import problem.MovingBox;
import problem.StaticObstacle;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * RRT algorithm for sampling configurations for boxes,
 * plans the path of a box.
 */
public class RRT {
    // Class Variables //
    private final int MAX_NODES = 1000000;
    private final double STEP_SIZE = 0.001;
    private List<Box> moveableObstacle;
    private List<StaticObstacle> staticObstacle;
    private Box currBox;
    private Point2D currPos;
    private Point2D goal;
    private LinkedList<Node> nodePath;
    private List<Node> allNodes = new ArrayList<>();

    // Constructors //

    /**
     * The constructor of RRT.
     * @param initState the current state
     * @param currBox the box to be path planned
     * @param goal the goal where the box has to be moved to
     */
    public RRT(State initState, MovingBox currBox, Point2D goal) {
        this.currBox = currBox;
        this.currPos = currBox.getPos();
        this.goal = goal;
        this.moveableObstacle = initState.getBoxes();
        moveableObstacle.addAll(initState.getMovingObst());
        this.staticObstacle = initState.getStaticObstSt();

        // Remove currBox from moveableObstacle
        moveableObstacle.remove(this.currBox);
    }

    // Methods //
    public void setNodePath() {
        Node start = new Node(currPos);
        Node end = new Node(goal);
        allNodes.add(start);
        nodePath = pathSearch(start);
    }

    public LinkedList<Node> pathSearch(Node start) {
        // Make random Node
        Random random = new Random();
        Point2D randPoint = new Point2D.Double(random.nextInt(1000) / 1000.0
                , random.nextInt(1000) / 1000.0);
        Node randNode = new Node(randPoint);

        // Find the nearest Node
        Node nearNode = randNode.nearestNode(allNodes);

        // Make new Node a STEP closer towards randNode from nearNode
        // STEP on the axis that is furtherest away from nearNode
        Node newNode;
        double xDist = randNode.getX() - nearNode.getX();
        double yDist = randNode.getY() - nearNode.getY();
        Point2D point;
        if (Math.abs(xDist) >= Math.abs(yDist)) {
            if (xDist > 0) {
                point = new Point2D.Double(nearNode.getX() + STEP_SIZE, nearNode.getY());
            } else {
                point = new Point2D.Double(nearNode.getX() - STEP_SIZE, nearNode.getY());
            }
        } else {
            if (yDist > 0) {
                point = new Point2D.Double(nearNode.getX(), nearNode.getY() + STEP_SIZE);
            } else {
                point = new Point2D.Double(nearNode.getX(), nearNode.getY() - STEP_SIZE);
            }
        }
        newNode = new Node(point);

        // Check if newNode pos is not blocked by Moveable Obstacles
        currBox.setRect(newNode.getCurrPos());
        for (Box obst : moveableObstacle) {
            if (currBox.getRect().intersects(obst.getRect().getX(), obst.getRect().getY()
                    , obst.getWidth(), obst.getWidth())) {
                // currBox intersects, so skip discard newNode, start a next random search
                return pathSearch(start);
            }
        }
        // Check if newNode pos is not blocked by Static Obstacles
        for (StaticObstacle obst : staticObstacle) {
            if (currBox.getRect().intersects(obst.getRect().getX(), obst.getRect().getY()
                    , obst.getRect().getWidth(), obst.getRect().getWidth())) {
                // currBox intersects, so skip discard newNode, start a next random search
                return pathSearch(start);
            }
        }
        // TODO: Check if there is enough room for robot to move around the box



        // All constraints has been checked and passed, add newNode to parent
        newNode.setParent(nearNode);
        allNodes.add(newNode);

        // check if newNode is a STEP next to goalNode, if it is, return a path
        if (newNode.nextToGoal()) {
            Node end = new Node(goal);
            end.setParent(newNode);

            // Backtrack from end Node, trace the path back to root
            Node nodeCheck = end;
            LinkedList<Node> path = new LinkedList<>();
            do {
                path.addFirst(nodeCheck);
                nodeCheck = nodeCheck.getParent();
            } while (nodeCheck != null);
            return path;

        } else if (allNodes.size() < MAX_NODES){
            // start another search
            return pathSearch(start);
        }

        // if this code is reached, it means allNode size is more than
        // the MAX_NODE number
        return null;
    }


    private class Node {
        // Class Variables //
        private Point2D currPos;
        private double xPos;
        private double yPos;
        private Node parent = null; // Only the root node will have null parent

        // Constructors //
        public Node(Point2D pos) {
            currPos = pos;
            xPos = pos.getX();
            yPos = pos.getY();
        }

        // Methods //
        public Point2D getCurrPos() {
            return currPos;
        }

        public double getX() {
            return xPos;
        }

        public double getY() {
            return yPos;
        }

        public Node nearestNode(List<Node> list) {
            double distance = 1;
            Node node = null;
            for (Node n : list) {
                Double length = n.getY() - this.getY();
                Double width = n.getX() - this.getY();
                Double hypot = Math.hypot(length, width);
                if (hypot < distance) {
                    distance = hypot;
                    node = n;
                }
            }
            return node;
        }

        public boolean nextToGoal() {
            if (xPos + STEP_SIZE == goal.getX() || xPos - STEP_SIZE == goal.getX()
            || yPos + STEP_SIZE == goal.getY() || yPos -  STEP_SIZE == goal.getY()) {
                return true;
            } else {
                return false;
            }
        }

        public void setParent(Node node) {
            parent = node;
        }

        public Node getParent() {
            return parent;
        }

    }

}
