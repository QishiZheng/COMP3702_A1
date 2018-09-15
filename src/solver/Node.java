package solver;

import java.awt.geom.Point2D;
import java.util.List;
import tester.Tester;

public class Node {
    // Class Variables //
    private Point2D currPos;
    private double xPos;
    private double yPos;
    private String direction = null; // Only the root node will have null direction
    private Node parent = null; // Only the root node will have null parent

    // Constructors //
    public  Node(Point2D pos) {
        currPos = pos;
        xPos = pos.getX();
        yPos = pos.getY();
    }

    // Methods //

    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            Node n = (Node) o;
            return n.getCurrPos().equals((currPos));
        }
        return false;
    }

    public  Point2D getCurrPos() {
        return currPos;
    }

    public double getX() {
        return xPos;
    }

    public double getY() {
        return yPos;
    }

    public Node nearestNode(List<Node> list) {
        double distance = 2000; // The maximum distance between 2 points in a 1x1 grid is sqrt(2)
        Node node = null;
        for (Node n : list) {
            Double length = Math.abs(n.getY() - this.getY());
            Double width = Math.abs(n.getX() - this.getY());
            if ((length + width) < distance) {
                distance = (length + width);
                node = n;
            }
        }
        return node;
    }

    public Node nextToList(List<Node> list) {
        for (Node n : list) {
            if ((xPos + Tester.MAX_BASE_STEP == n.getX() && yPos == n.getY())
                    || (xPos - Tester.MAX_BASE_STEP == n.getX() && yPos == n.getY())
                    || (yPos + Tester.MAX_BASE_STEP == n.getY() && xPos == n.getX())
                    || (yPos - Tester.MAX_BASE_STEP == n.getY() && xPos == n.getX())) {
                return n;
            }
        }
        return null;
    }

    public void setParent(Node node) {
        parent = node;
    }

    public Node getParent() {
        return parent;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        if (this.direction == null) {
            this.direction = direction;
        }
    }
}
