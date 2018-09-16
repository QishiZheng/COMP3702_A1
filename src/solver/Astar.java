package solver;

import problem.*;
import tester.Tester;

import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Astart search algorithm for searching shortest path in the state graph.
 */
public class Astar {
    // Class Variables //
    private final double STEP_SIZE = Tester.MAX_BASE_STEP;
    private final DecimalFormat DECIMAL = new DecimalFormat("0.###");
    private List<Point2D> goalList = new ArrayList<>();
    private List<Box> mvBox = new ArrayList<>();
    private List<Box> movedBox =  new ArrayList<>();
    private List<Box> mvObst = new ArrayList<>();
    private List<StaticObstacle> staticObst = new ArrayList<>();
    private Point2D start;
    private Point2D end;
    private double width;
    private double robotWidth;
    private List<Node> opened = new ArrayList<>();
    private List<Node> closed = new ArrayList<>();
    private List<LinkedList<Point2D>> mvBoxPaths =  new ArrayList<>();
    private List<LinkedList<Point2D>> robotPaths = new ArrayList<>();

    // Constructors //
    public Astar(ProblemSpec ps) {
        setMvBox(ps.getMovingBoxes());
        setMvObst(ps.getMovingObstacles());
        setStaticObst(ps.getStaticObstacles());
        setGoalList(ps.getMovingBoxEndPositions());
        setPath();
    }

    // Methods //
    private void setMvBox(List<Box> mvBoxList) {
        for (Box box : mvBoxList) {
            Point2D point = new Point2D.Double(formatDouble(box.getPos().getX()), formatDouble(box.getPos().getY()));
            Box parsedBox = new MovingBox(point, formatDouble(box.getWidth()));
            mvBox.add(parsedBox);
        }
    }

    private void setMvObst(List<Box> mvObstList) {
        for (Box obst : mvObstList) {
            Point2D point = new Point2D.Double(formatDouble(obst.getPos().getX()), formatDouble(obst.getPos().getY()));
            Box parsedBox = new MovingObstacle(point, formatDouble(obst.getWidth()));
            mvObst.add(parsedBox);
        }
    }

    private void setStaticObst(List<StaticObstacle> staticObstList) {
        for (StaticObstacle so : staticObstList) {
            StaticObstacle parsedBox = new StaticObstacle(formatDouble(so.getRect().getX())
                    , formatDouble(so.getRect().getY()), formatDouble(so.getRect().getWidth())
                    , formatDouble(so.getRect().getHeight()));
            staticObst.add(parsedBox);
        }
    }

    private void setGoalList(List<Point2D> list) {
        for (Point2D point : list) {
            Point2D parsedPoint = new Point2D.Double(formatDouble(point.getX()), formatDouble(point.getY()));
            goalList.add(parsedPoint);
        }
    }

    private void initBoxData() {
        Box box = mvBox.remove(0);
        System.out.println("Box selected: " + box.getPos().toString());
        start = box.getPos();
        width = box.getWidth();
        robotWidth = width;
        end = goalList.remove(0);
        System.out.println("Box goal: " + end.toString());

        Node root = new Node(start, null);
        opened.add(root);
    }

    private void updateMovedBox(boolean bool) {
        if (bool) {
            Box box = new MovingBox(end, width);
            movedBox.add(box);
        } else {
            Box box = new MovingBox(start, width);
            movedBox.add(box);
        }
        opened.clear();
        closed.clear();
    }

    private void setPath() {
        while (!mvBox.isEmpty()) {
            initBoxData();
            LinkedList<Point2D> path = pathSearch();
            mvBoxPaths.add(path);
            if (path != null) {
                System.out.println("path exists");
                updateMovedBox(true);
            } else {
                updateMovedBox(false);
                System.out.println("path does not exist");
            }
        }
    }

    private LinkedList<Point2D> pathSearch() {
        while (!opened.isEmpty()) {
            ////System.out.println("# in opened: " + opened.size());
            // Get the node with the lowest F-Cost
            Node q = null;
            for (Node n : opened) {
                ////System.out.println(n.getCurrPos().toString() + " has F: " + n.getF());
                if (q == null) {
                    q = n;
                    ////System.out.println("default node selection: " + q.getCurrPos().toString());
                } else if (n.getF() <= q.getF()) {
                    q = n;
                    ////System.out.println("replaced default with: " + q.getCurrPos().toString());
                }
            }
            ////System.out.println("node selected: " + q.getCurrPos().toString());
            // Remove node from opened list
            opened.remove(q);

            // Get children of q
            List<Node> list = q.getChildren();
            ////System.out.println("child U: " + list.get(0).getCurrPos().toString());
            ////System.out.println("child D: " + list.get(1).getCurrPos().toString());
            ////System.out.println("child L: " + list.get(2).getCurrPos().toString());
            ////System.out.println("child R: " + list.get(3).getCurrPos().toString());

            // For each child,
            // 1) check is goal,
            // 2) if child is already on openedList,
            // 3) if child is already in closedList under different parent and has lower F
            // 4) check if the point is not an obstacle
            // 5) check if robot has space to move
            for (Node n : list) {
                // Check if node is goal
                if (n.getCurrPos().equals(end)) {
                    Node nodeCheck = n;
                    LinkedList<Point2D> path = new LinkedList<>();
                    LinkedList<Point2D> roboPath = new LinkedList<>();
                    do {
                        path.addFirst(nodeCheck.getCurrPos());
                        nodeCheck = nodeCheck.getParent();
                    } while (nodeCheck != null);
                    return path;
                } else if (!opened.contains(n) && !closed.contains(n) && !isMvBox(n.currBox)
                        && !isMvObst(n.currBox) && !isStaticObst(n.currBox) && !isOutOfBounds(n.currBox)
                        && !n.hasNoRoom()) {
                    ////System.out.println("child: " + n.getCurrPos().toString());
                    ////System.out.println("in closed: " + n.inClosed());
                    ////System.out.println("added to opened: " + n.getCurrPos().toString());
                    opened.add(n);
                }
                ////System.out.println("node skipped: " + n.getCurrPos().toString());
            }
            closed.add(q);
            ////System.out.println("node added to closed: " + q.getCurrPos().toString());
            ////System.out.println("# in closed: " + closed.size());
        }

        return null;
    }



    private boolean isMvBox(Box currBox) {
        // Check the mvBox list
        for (Box mb : mvBox) {
            if (currBox.getRect().intersects(formatDouble(mb.getRect().getX()), formatDouble(mb.getRect().getY())
                    , formatDouble(mb.getWidth()), formatDouble(mb.getWidth()))) {
                // currBox intersects, so discard newNode, start a next random search
                ////System.out.println("Moving Box at: " + currBox.getPos().toString());
                return true;
            }
        }

        // Check the movedBox list
        for (Box mb : movedBox) {
            if (currBox.getRect().intersects(formatDouble(mb.getRect().getX()), formatDouble(mb.getRect().getY())
                    , formatDouble(mb.getWidth()), formatDouble(mb.getWidth()))) {
                // currBox intersects, so discard newNode, start a next random search
                ////System.out.println("Moved Box at: " + currBox.getPos().toString());
                return true;
            }
        }
        return false;
    }

    private boolean isMvObst(Box currBox) {
        for (Box mo : mvObst) {
            if (currBox.getRect().intersects(formatDouble(mo.getRect().getX()), formatDouble(mo.getRect().getY())
                    , formatDouble(mo.getWidth()), formatDouble(mo.getWidth()))) {
                // currBox intersects, so discard newNode, start a next random search
                System.out.println("Moving Obst at: " + currBox.getPos().toString());
                return true;
            }
        }
        return false;
    }

    private boolean isStaticObst(Box currBox) {
        for (StaticObstacle so : staticObst) {
            if (currBox.getRect().intersects(formatDouble(so.getRect().getX()), formatDouble(so.getRect().getY())
                    , formatDouble(so.getRect().getWidth()), formatDouble(so.getRect().getWidth()))) {
                // currBox intersects, so discard newNode, start a next random search
                System.out.println("Static Obst at: " + currBox.getPos().toString());
                return true;
            }
        }
        return false;
    }

    private boolean isOutOfBounds(Box currBox) {
        if ((formatDouble(currBox.getRect().getMaxX()) > 1) || (formatDouble(currBox.getRect().getMaxY()) > 1)
                || (formatDouble(currBox.getRect().getMinX()) < 0) || (formatDouble(currBox.getRect().getMinY()) < 0)) {
            System.out.println("Out of bounds at: " + currBox.getPos().toString());
            return true;
        }
        return false;
    }

    public List<LinkedList<Point2D>> getMvBoxPaths() {
        return mvBoxPaths;
    }

    private double formatDouble(double number) {
        return Double.parseDouble(DECIMAL.format(number));
    }










    private class Node {
        // Class Variables //
        private Point2D currPos;
        private Box currBox;
        private double fCost;
        private double gCost;
        private double hCost;
        private String direction = null; // Only the root node will have null direction
        private Node parent; // The root node will have null parent
        private List<Point2D> children = new ArrayList<>();

        // Constructors //
        private Node(Point2D pos, Node parent) {
            currPos = pos;
            currBox = new MovingBox(currPos, width);
            this.parent = parent;
            setCosts();
            setChildren();
        }

        // Methods //

        @Override
        public boolean equals(Object o) {
            if (o instanceof Node) {
                Node n = (Node) o;
                return n.getCurrPos().equals(currPos);
            }
            return false;
        }

        private Point2D getCurrPos() {
            return currPos;
        }

        private double getF() {
            return fCost;
        }

        private double getG() {
            return gCost;
        }

        private double getH() {
            return  hCost;
        }

        private Node getParent() {
            return parent;
        }

        private void setG() {
            if (parent != null) {
                gCost = formatDouble(parent.getG() + STEP_SIZE);
            } else {
                gCost = 0;
            }
        }

        private void setH() {
            hCost = formatDouble(Math.abs(end.getX() - currPos.getX())
                    + Math.abs(end.getY() - currPos.getY()));
        }

        private void setF() {
            fCost = formatDouble(gCost + hCost);
        }

        private void setCosts() {
            setG();
            setH();
            setF();
        }

        private void setChildren() {
            // order = [u, d, l ,r]
            Point2D u = new Point2D.Double(formatDouble(currPos.getX())
                    , formatDouble(currPos.getY() - STEP_SIZE));
            Point2D d = new Point2D.Double(formatDouble(currPos.getX())
                    , formatDouble(currPos.getY() + STEP_SIZE));
            Point2D l = new Point2D.Double(formatDouble(currPos.getX() - STEP_SIZE)
                    , formatDouble(currPos.getY()));
            Point2D r = new Point2D.Double(formatDouble(currPos.getX() + STEP_SIZE)
                    , formatDouble(currPos.getY()));
            children.add(u);
            children.add(d);
            children.add(l);
            children.add(r);
        }

        private List<Node> getChildren() {
            Node u = new Node(children.get(0), this);
            u.setDirection("u");
            Node d = new Node(children.get(1), this);
            d.setDirection("d");
            Node l = new Node(children.get(2), this);
            l.setDirection("l");
            Node r = new Node(children.get(3), this);
            r.setDirection("r");

            List<Node> list = new ArrayList<>();
            list.add(u);
            list.add(d);
            list.add(l);
            list.add(r);

            return list;
        }

        private void setDirection(String direction) {
            if (this.direction == null) {
                this.direction = direction;
            }
        }

        private String getDirection() {
            return direction;
        }

        @Deprecated
        private boolean lessFCostInOpened() {
            for (Node n : opened) {
                if (this.equals(n) && (n.getF() <= this.fCost)) {
                    return true;
                }
            }
            return false;
        }

        @Deprecated
        private boolean inClosed() {
            for (Node n : closed) {
                if (this.equals(n)) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasNoRoom() {
            if (parent.getDirection() == null) {
                return false;
            } else if ((parent.getDirection().equals("r") && direction.equals("u"))
                    || (parent.getDirection().equals("u") && direction.equals("r"))) {
                Point2D leftBoxPoint = new Point2D.Double(formatDouble((currBox.getRect().getX() - (width / 2)))
                        , formatDouble(currBox.getRect().getCenterY()));
                Point2D downBoxPoint = new Point2D.Double(formatDouble(currBox.getRect().getX())
                        , formatDouble((currBox.getRect().getY() + width)));
                Box leftBox = new MovingBox(leftBoxPoint, formatDouble((robotWidth / 2)));
                Box downBox = new MovingBox(downBoxPoint, formatDouble((robotWidth / 2)));

                return (isMvBox(leftBox) || isMvObst(leftBox) || isStaticObst(leftBox) || isOutOfBounds(leftBox)
                        || isMvBox(downBox) || isMvObst(downBox) || isStaticObst(downBox) || isOutOfBounds(downBox));
            } else if ((parent.getDirection().equals("r") && direction.equals("d"))
                    || (parent.getDirection().equals("d") && direction.equals("r"))) {
                Point2D leftBoxPoint = new Point2D.Double(formatDouble((currBox.getRect().getX() - (width / 2)))
                        , formatDouble(currBox.getRect().getY()));
                Point2D upBoxPoint = new Point2D.Double(formatDouble(currBox.getRect().getX())
                        , formatDouble((currBox.getRect().getY() - (width / 2))));
                Box leftBox = new MovingBox(leftBoxPoint, formatDouble((robotWidth / 2)));
                Box upBox = new MovingBox(upBoxPoint, formatDouble((robotWidth / 2)));

                return (isMvBox(leftBox) || isMvObst(leftBox) || isStaticObst(leftBox) || isOutOfBounds(leftBox)
                        || isMvBox(upBox) || isMvObst(upBox) || isStaticObst(upBox) || isOutOfBounds(upBox));
            } else if ((parent.getDirection().equals("l") && direction.equals("u"))
                    || (parent.getDirection().equals("u") && direction.equals("l"))) {
                Point2D rightBoxPoint = new Point2D.Double(formatDouble((currBox.getRect().getX() + width))
                        , formatDouble(currBox.getRect().getCenterY()));
                Point2D downBoxPoint = new Point2D.Double(formatDouble(currBox.getRect().getCenterX())
                        , formatDouble((currBox.getRect().getY() + width)));
                Box rightBox = new MovingBox(rightBoxPoint, formatDouble((robotWidth / 2)));
                Box downBox = new MovingBox(downBoxPoint, formatDouble((robotWidth / 2)));

                return (isMvBox(rightBox) || isMvObst(rightBox) || isStaticObst(rightBox) || isOutOfBounds(rightBox)
                        || isMvBox(downBox) || isMvObst(downBox) || isStaticObst(downBox) || isOutOfBounds(downBox));
            } else if ((parent.getDirection().equals("l") && direction.equals("d"))
                    || (parent.getDirection().equals("d") && direction.equals("l"))) {
                Point2D rightBoxPoint = new Point2D.Double(formatDouble((currBox.getRect().getX() + width))
                        , formatDouble(currBox.getRect().getY()));
                Point2D upBoxPoint = new Point2D.Double(formatDouble(currBox.getRect().getCenterX())
                        , formatDouble((currBox.getRect().getY() - width)));
                Box rightBox = new MovingBox(rightBoxPoint, formatDouble((robotWidth / 2)));
                Box upBox = new MovingBox(upBoxPoint, formatDouble((robotWidth / 2)));

                return (isMvBox(rightBox) || isMvObst(rightBox) || isStaticObst(rightBox) || isOutOfBounds(rightBox)
                        || isMvBox(upBox) || isMvObst(upBox) || isStaticObst(upBox) || isOutOfBounds(upBox));
            }

            return false;

        }
    }
}

