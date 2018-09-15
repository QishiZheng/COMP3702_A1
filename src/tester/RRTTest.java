package tester;

import problem.Box;
import problem.MovingBox;
import problem.MovingObstacle;
import problem.StaticObstacle;
import solver.RRT;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RRTTest {


    public static void main(String[] args) {
        // Configure here //
        // Box Starting Point
        Point2D testBoxPoint = new Point2D.Double(0.15, 0.15);
        Box testBox = new MovingBox(testBoxPoint, 0.1);

        // Box Ending Point
        Point2D goal = new Point2D.Double(0.5, 0.210);

        // Moveable Box Points
        Point2D testMoveBoxPoint = new Point2D.Double(0.25, 0.25);
        MovingBox obstBox = new MovingBox(testMoveBoxPoint, 0.1);

        List<MovingBox> boxList = new ArrayList<>();
        boxList.add(obstBox);

        // Moveable Obstacle Points
        Point2D testMoveBoxPoint2 = new Point2D.Double(0.4, 0.4);
        MovingObstacle movingObst = new MovingObstacle(testMoveBoxPoint2, 0.15);

        List<MovingObstacle> movingObstacleList = new ArrayList<>();
        movingObstacleList.add(movingObst);

        // Static Obstacles
        StaticObstacle staticObst = new StaticObstacle("0.0 0.8 0.3 1.0");
        // StaticObstacle staticObst2 = new StaticObstacle(0.501, 0.205, 0.499, 0.001);


        List<StaticObstacle> staticObstacleList = new ArrayList<>();
        staticObstacleList.add(staticObst);
        // staticObstacleList.add(staticObst2);

        // End Configuration //

        RRT rrt = new RRT(testBox, goal, boxList, movingObstacleList, staticObstacleList);
        LinkedList<Point2D> coordPath = rrt.getCoordPath();

        if (coordPath != null) {
            for (Point2D coord : coordPath) {
                System.out.println("X: " + coord.getX() + ", Y: " + coord.getY());
            }
        } else {
            System.out.println("path is null");
        }
    }
}