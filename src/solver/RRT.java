package solver;

import problem.Box;
import problem.MovingBox;
import problem.MovingObstacle;
import problem.StaticObstacle;
import tester.Tester;

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
    private final double STEP_SIZE = Tester.MAX_BASE_STEP;
    private final DecimalFormat DECIMAL = new DecimalFormat("0.###");
    private List<MovingBox> moveableBox;
    private List<MovingObstacle> moveableObstacle;
    private List<StaticObstacle> staticObstacle;
    private Node start;
    private Node end;
    private double width;
    private LinkedList<Point2D> coordPath;
    private List<Node> startList = new ArrayList<>();
    private List<Node> endList = new ArrayList<>();
    private List<Node> allNodes = new ArrayList<>();


    // Constructors //

    /**
     * The constructor of RRT.
     * @param currBox the box to be path planned (bottom left aligned coord)
     * @param goal the goal where the box has to be moved to (bottom left aligned coord)
     * @param moveableObstacle list of moveableObstacles, include moving boxes and moving obstacles
     * @param staticObstacle list of staticObstacle
     */
    public RRT(Box currBox, Point2D goal, List<MovingBox> moveableBox, List<MovingObstacle> moveableObstacle
            , List<StaticObstacle> staticObstacle) {
        start = new Node(currBox.getPos());
        end = new Node(goal);
        width = currBox.getWidth();
        this.moveableBox = moveableBox;
        this.moveableObstacle = moveableObstacle;
        this.staticObstacle = staticObstacle;

        this.setCoordPath();
    }

    // Methods //

    /**
     * initializes coordPath filling it with the solution path
     */
    private void setCoordPath() {
        addToList(startList, start);
        addToList(endList, end);
        coordPath = pathSearch();
    }

    /**
     * the main algorithm of RRT, switches between finding a path from start and goal.
     * @return a LinkedList filled with Point2D objects that forms a path from start to goal.
     */
    private LinkedList<Point2D> pathSearch() {
        // flip determines whether to expand the path from start node or goal node. False = start, True = goal
        boolean flip = false;
        loop:
        while (allNodes.size() < MAX_NODES) {
            // Make random Node
            Random random = new Random();
            Point2D randPoint = new Point2D.Double(
                    Double.parseDouble(DECIMAL.format(random.nextInt(1000) * 0.001))
                    , Double.parseDouble(DECIMAL.format(random.nextInt(1000) * 0.001)));
            Node randNode = new Node(randPoint);

            // Find the nearest Node
            Node nearNode;
            if (flip) {
                nearNode = randNode.nearestNode(endList);
            } else {
                nearNode = randNode.nearestNode(startList);
            }


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
                            Double.parseDouble(DECIMAL.format(nearNode.getX() + STEP_SIZE))
                            , Double.parseDouble(DECIMAL.format(nearNode.getY())));
                    direction = "r";
                } else {
                    point = new Point2D.Double(
                            Double.parseDouble(DECIMAL.format(nearNode.getX() - STEP_SIZE))
                            , Double.parseDouble(DECIMAL.format(nearNode.getY())));
                    direction = "l";
                }
            } else {
                if (yDist > 0) {
                    point = new Point2D.Double(
                            Double.parseDouble(DECIMAL.format(nearNode.getX()))
                            , Double.parseDouble(DECIMAL.format(nearNode.getY() + STEP_SIZE)));
                    direction = "d";
                } else {
                    point = new Point2D.Double(
                            Double.parseDouble(DECIMAL.format(nearNode.getX()))
                            , Double.parseDouble(DECIMAL.format(nearNode.getY() - STEP_SIZE)));
                    direction = "u";
                }
            }
            newNode = new Node(point);
            newNode.setParent(nearNode);
            newNode.setDirection(direction);

            // Check if newNode pos is not blocked by Moveable Boxes and robot has enough room to move
            Box testBox = new MovingBox(newNode.getCurrPos(), width);
            for (MovingBox obst : moveableBox) {
                if (testBox.getRect().intersects(obst.getRect().getX(), obst.getRect().getY()
                        , obst.getWidth(), obst.getWidth())) {
                    // currBox intersects, so discard newNode, start a next random search
                    continue loop;
                } else if (notEnoughRoom(testBox.getRect(), obst.getRect(), nearNode, newNode)) {
                    continue loop;
                }
            }
            // Check if newNode pos is not blocked by Moveable Obstacle and robot has enough room to move
            for (MovingObstacle obst : moveableObstacle) {
                if (testBox.getRect().intersects(obst.getRect().getX(), obst.getRect().getY()
                        , obst.getWidth(), obst.getWidth())) {
                    // currBox intersects, so discard newNode, start a next random search
                    continue loop;
                } else if (notEnoughRoom(testBox.getRect(), obst.getRect(), nearNode, newNode)) {
                    continue loop;
                }
            }
            // Check if newNode pos is not blocked by Static Obstacles
            for (StaticObstacle obst : staticObstacle) {
                if (testBox.getRect().intersects(obst.getRect().getX(), obst.getRect().getY()
                        , obst.getRect().getWidth(), obst.getRect().getWidth())) {
                    // currBox intersects, so skip discard newNode, start a next random search
                    continue loop;
                } else if (notEnoughRoom(testBox.getRect(), obst.getRect(), nearNode, newNode)) {
                    continue loop;
                }
            }
            // Check if newNode already exists in allNodes
            for (Node n : allNodes) {
                if (n.equals(newNode)) {
                    continue loop;
                }
            }

            // All constraints has been checked and passed, add newNode to list of all nodes
            if (flip) {
                addToList(endList, newNode);
            } else {
                addToList(startList, newNode);
            }

            // check if newNode is a STEP next to nodes in list2, if it is, return a path
            Node adjNode;
            if (flip){
                adjNode = newNode.nextToList(startList);
            } else {
                adjNode = newNode.nextToList(endList);
            }
            if (adjNode != null) {
                // Backtrack from newNode, trace the path back to root
                Node nodeCheck = newNode;
                LinkedList<Point2D> path1 = new LinkedList<>();
                do {
                    path1.addFirst(nodeCheck.getCurrPos());
                    nodeCheck = nodeCheck.getParent();
                } while (nodeCheck != null);

                // Backtrack from adjNode, trace the path back to root
                nodeCheck = adjNode;
                LinkedList<Point2D> path2 = new LinkedList<>();
                do {
                    path2.addLast(nodeCheck.getCurrPos());
                    nodeCheck = nodeCheck.getParent();
                } while (nodeCheck != null);
                path1.addAll(path2);

                // Check if the list is back to front
                if (path1.getFirst().equals(end.getCurrPos())) {
                    Collections.reverse(path1);
                }
                return path1;

            } else {
                // start another search, switch list around
                flip = !flip;
            }
        }
        // if this code is reached, it means allNode size is more than
        // the MAX_NODE number
        return null;
    }

    /**
     * add a specific Node to the specified list (either startList or endList) and also add it to allNodes list
     * @param list the list to add Node to
     * @param node the Node to add to the list
     */
    private void addToList(List<Node> list, Node node) {
        list.add(node);
        allNodes.add(node);
    }

    /**
     * check if the given box has enough room for the robot to rotate in relation to the given obstacle.
     * @param box the box that robot has to rotate around
     * @param obst the obstacle to be checked if robot is not blocked by it
     * @param parent the parent node to check if robot requires change of direction
     * @param child the child node to check if robot requires change of direction
     * @return True if not enough room, False if enough room
     */
    private boolean notEnoughRoom(Rectangle2D box, Rectangle2D obst, Node parent, Node child) {
        if (parent.getDirection() == null) {
            return false;
            // Case 1: Robot switch from left side of box to bot side or vice versa,
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


    /**
     * A private Node class that stores the required information about each node in RRT
     */
    public class Node {
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

        private  Point2D getCurrPos() {
            return currPos;
        }

        private double getX() {
            return xPos;
        }

        private double getY() {
            return yPos;
        }

        /**
         * Search for the closest Node from the given list in relation to this Node.
         * @param list the list to be searched
         * @return the closest Node from the list
         */
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

        /**
         * search for any Node in the given list that is STEP_SIZE away from this Node
         * @param list the list to be searched with
         * @return the Node that is STEP_SIZE away from this Node, if there is none returns null
         */
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

        /**
         * the direction the robot has to in relation to this Node's parent.
         * @param direction u = up, d = down, l = left, r = right
         */
        private void setDirection(String direction) {
            if (this.direction == null) {
                this.direction = direction;
            }
        }
    }

}