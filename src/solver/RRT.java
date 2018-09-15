package solver;

import problem.Box;
import problem.MovingBox;
import problem.StaticObstacle;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
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
    private Node start;
    private Node end;
    private double width;
    private LinkedList<Point2D> coordPath;
    private List<Node> startList = new ArrayList<>();
    private List<Node> endList = new ArrayList<>();
    private List<Node> allNodes = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("0.###");

    // Constructors //

    /**
     * The constructor of RRT.
     * @param currBox the box to be path planned (bottom left aligned coord)
     * @param goal the goal where the box has to be moved to (bottom left aligned coord)
     * @param moveableObstacle list of moveableObstacles, include moving boxes and moving obstacles
     * @param staticObstacle list of staticObstacle
     */
    public RRT(Box currBox, Point2D goal, List<Box> moveableObstacle
            , List<StaticObstacle> staticObstacle) {
        start = new Node(currBox.getPos());
        end = new Node(goal);
        width = currBox.getWidth();
        this.moveableObstacle = moveableObstacle;
        this.staticObstacle = staticObstacle;

        this.setCoordPath();
    }

    // Methods //
    private void setCoordPath() {
        addToList(startList, start);
        addToList(endList, end);
        coordPath = pathSearch(startList, endList);
    }

    private LinkedList<Point2D> pathSearch(List<Node> list1, List<Node> list2) {
        // Make random Node
        Random random = new Random();
        Point2D randPoint = new Point2D.Double(
                Double.parseDouble(df.format(random.nextInt(1000) * 0.001))
                , Double.parseDouble(df.format(random.nextInt(1000) * 0.001)));
        ///// System.out.println("random at X: " + randPoint.getX() + ", Y: " + randPoint.getY());
        Node randNode = new Node(randPoint);

        // Find the nearest Node
        Node nearNode = randNode.nearestNode(list1);

        // Make new Node a STEP_SIZE closer towards randNode from nearNode
        // step on the axis that is furtherest away from nearNode
        Node newNode;
        double xDist = randNode.getX() - nearNode.getX();
        double yDist = randNode.getY() - nearNode.getY();
        Point2D point;
        String direction;
        if (Math.abs(xDist) >= Math.abs(yDist)) {
            if (xDist > 0) {
                point = new Point2D.Double(
                        Double.parseDouble(df.format(nearNode.getX() + STEP_SIZE))
                        , Double.parseDouble(df.format(nearNode.getY())));
                direction = "r";
            } else {
                point = new Point2D.Double(
                        Double.parseDouble(df.format(nearNode.getX() - STEP_SIZE))
                        , Double.parseDouble(df.format(nearNode.getY())));
                direction = "l";
            }
        } else {
            if (yDist > 0) {
                point = new Point2D.Double(
                        Double.parseDouble(df.format(nearNode.getX()))
                        , Double.parseDouble(df.format(nearNode.getY() + STEP_SIZE)));
                direction = "d";
            } else {
                point = new Point2D.Double(
                        Double.parseDouble(df.format(nearNode.getX()))
                        , Double.parseDouble(df.format(nearNode.getY() - STEP_SIZE)));
                direction = "u";
            }
        }
        newNode = new Node(point);
        newNode.setParent(nearNode);
        newNode.setDirection(direction);

        // Check if newNode pos is not blocked by Moveable Obstacles and robot has enough room to move
        Box testBox = new MovingBox(newNode.getCurrPos(), width);
        for (Box obst : moveableObstacle) {
            if (testBox.getRect().intersects(obst.getRect().getX(), obst.getRect().getY()
                    , obst.getWidth(), obst.getWidth())) {
                // currBox intersects, so discard newNode, start a next random search
                return pathSearch(list1, list2);
            } else if (notEnoughRoom(testBox.getRect(), obst.getRect(), nearNode, newNode)) {
                return pathSearch(list1, list2);
            }
        }
        // Check if newNode pos is not blocked by Static Obstacles
        for (StaticObstacle obst : staticObstacle) {
            if (testBox.getRect().intersects(obst.getRect().getX(), obst.getRect().getY()
                    , obst.getRect().getWidth(), obst.getRect().getWidth())) {
                // currBox intersects, so skip discard newNode, start a next random search
                return pathSearch(list1, list2);
            } else if (notEnoughRoom(testBox.getRect(), obst.getRect(), nearNode, newNode)) {
                return pathSearch(list1, list2);
            }
        }
        // Check if newNode already exists in allNodes
        for (Node n : allNodes) {
            if (n.equals(newNode)) {
                return pathSearch(list1, list2);
            }
        }

        // All constraints has been checked and passed, add newNode to list of all nodes
        addToList(list1, newNode);
        System.out.println("Added point: " + newNode.getX() + ", " + newNode.getY());

        // check if newNode is a STEP next to nodes in list2, if it is, return a path
        Node adjNode = newNode.nextToList(list2);
        if (adjNode != null) {
            // Backtrack from newNode, trace the path back to root
            Node nodeCheck = newNode;
            LinkedList<Point2D> path1 = new LinkedList<>();
            do {
                path1.addFirst(nodeCheck.currPos);
                nodeCheck = nodeCheck.getParent();
            } while (nodeCheck != null);

            // Backtrack from adjNode, trace the path back to root
            nodeCheck = adjNode;
            LinkedList<Point2D> path2 = new LinkedList<>();
            do {
                path2.addLast(nodeCheck.currPos);
                nodeCheck = nodeCheck.getParent();
            } while (nodeCheck != null);
            path1.addAll(path2);

            // Check if the list is back to front
            if (path1.getFirst().equals(end.currPos)) {
                Collections.reverse(path1);
            }
            return path1;

        } else if (allNodes.size() < MAX_NODES){
            // start another search, switch list around
            return pathSearch(list2, list1);
        }

        // if this code is reached, it means allNode size is more than
        // the MAX_NODE number
        return null;
    }

    private void addToList(List<Node> list, Node node) {
        list.add(node);
        allNodes.add(node);
    }

    private boolean notEnoughRoom(Rectangle2D box, Rectangle2D obst, Node parent, Node child) {
        // Case 1: Robot switch from left side of box to bot side or vice versa,
        if (parent.getDirection() == null) {
            return false;
        } else if ((parent.getDirection().equals("r") && child.getDirection().equals("u"))
                || (parent.getDirection().equals("u") && child.getDirection().equals("r"))) {
            Point2D leftBoxPoint = new Point2D.Double((box.getX() - (box.getWidth() / 2)), box.getCenterY());
            Point2D downBoxPoint = new Point2D.Double(box.getX(), (box.getY() + box.getWidth()));
            Box leftBox = new MovingBox(leftBoxPoint, (box.getWidth() / 2));
            Box downBox = new MovingBox(downBoxPoint, (box.getWidth() / 2));

            return (leftBox.getRect().intersects(obst.getX(), obst.getY(), obst.getWidth(), obst.getWidth())
                    || downBox.getRect().intersects(obst.getX(), obst.getY(), obst.getWidth(), obst.getWidth()));

          // Case 2: Robot switch from left side of box to top side or vice versa.
        } else if ((parent.getDirection().equals("r") && child.getDirection().equals("d"))
                || (parent.getDirection().equals("d") && child.getDirection().equals("r"))) {
            Point2D leftBoxPoint = new Point2D.Double((box.getX() - (box.getWidth() / 2)), box.getY());
            Point2D upBoxPoint = new Point2D.Double(box.getX(), (box.getY() - (box.getWidth() / 2)));
            Box leftBox = new MovingBox(leftBoxPoint, (box.getWidth() / 2));
            Box upBox = new MovingBox(upBoxPoint, (box.getWidth() / 2));

            return (leftBox.getRect().intersects(obst.getX(), obst.getY(), obst.getWidth(), obst.getWidth())
                    || upBox.getRect().intersects(obst.getX(), obst.getY(), obst.getWidth(), obst.getWidth()));
          // Case 3: Robot switch from right side of box to bot side or vice versa.
        } else if ((parent.getDirection().equals("l") && child.getDirection().equals("u"))
                || (parent.getDirection().equals("u") && child.getDirection().equals("l"))) {
            Point2D rightBoxPoint = new Point2D.Double((box.getX() + box.getWidth()), box.getCenterY());
            Point2D downBoxPoint = new Point2D.Double(box.getCenterX(), (box.getY() + box.getWidth()));
            Box rightBox = new MovingBox(rightBoxPoint, (box.getWidth() / 2));
            Box downBox = new MovingBox(downBoxPoint, (box.getWidth() / 2));

            return rightBox.getRect().intersects(obst.getX(), obst.getY(), obst.getWidth(), obst.getWidth())
                    || downBox.getRect().intersects(obst.getX(), obst.getY(), obst.getWidth(), obst.getWidth());
          // Case 4: Robot switch frm right side of box to top side or vice versa.
        } else if ((parent.getDirection().equals("l") && child.getDirection().equals("d"))
                || (parent.getDirection().equals("d") && child.getDirection().equals("l"))) {
            Point2D rightBoxPoint = new Point2D.Double((box.getX() + box.getWidth()), box.getY());
            Point2D upBoxPoint = new Point2D.Double(box.getCenterX(), (box.getY() - box.getWidth()));
            Box rightBox = new MovingBox(rightBoxPoint, (box.getWidth() / 2));
            Box upBox = new MovingBox(upBoxPoint, (box.getWidth() / 2));

            return rightBox.getRect().intersects(obst.getX(), obst.getY(), obst.getWidth(), obst.getWidth())
                    || upBox.getRect().intersects(obst.getX(), obst.getY(), obst.getWidth(), obst.getWidth());
          // Case 5: robot not switching sides
        } else {
            return false;
        }
    }

    public LinkedList<Point2D> getCoordPath() {
        return coordPath;
    }


    private class Node {
        // Class Variables //
        private Point2D currPos;
        private double xPos;
        private double yPos;
        private String direction = null; // Only the root node will have null direction
        private Node parent = null; // Only the root node will have null parent

        // Constructors //
        private Node(Point2D pos) {
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

        private Point2D getCurrPos() {
            return currPos;
        }

        private double getX() {
            return xPos;
        }

        private double getY() {
            return yPos;
        }

        private Node nearestNode(List<Node> list) {
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

        private Node nextToList(List<Node> list) {
            for (Node n : list) {
                if ((xPos + STEP_SIZE == n.getX() && yPos == n.getY())
                        || (xPos - STEP_SIZE == n.getX() && yPos == n.getY())
                        || (yPos + STEP_SIZE == n.getY() && xPos == n.getX())
                        || (yPos - STEP_SIZE == n.getY() && xPos == n.getX())) {
                    return n;
                }
            }
            return null;
        }

        private void setParent(Node node) {
            parent = node;
        }

        private Node getParent() {
            return parent;
        }

        private String getDirection() {
            return direction;
        }

        private void setDirection(String direction) {
            if (this.direction == null) {
                this.direction = direction;
            }
        }
    }

}
