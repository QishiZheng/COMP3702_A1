package tester;

import problem.Box;
import problem.MovingBox;
import problem.StaticObstacle;
import solver.RRT;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RRTTest {


    public static void main(String[] args) {
        // Configure here //
        // Box width
        double width = 0.002;

        // Box Starting Point
        Point2D testBoxPoint = new Point2D.Double(0.5, 0.200);
        Box testBox = new MovingBox(testBoxPoint, width);

        // Box Ending Point
        Point2D goal = new Point2D.Double(0.5, 0.210);

        // Moveable Obstacles Points
        Point2D testMoveBoxPoint = new Point2D.Double(0.5, 0.1);
        Box obstBox = new MovingBox(testMoveBoxPoint, width);


        List<Box> boxList = new ArrayList<>();
        //boxList.add(obstBox);

        // Static Obstacles
        StaticObstacle staticObst = new StaticObstacle(0, 0.205, 0.499, 0.001);
        StaticObstacle staticObst2 = new StaticObstacle(0.501, 0.205, 0.499, 0.001);


        List<StaticObstacle> staticObstacleList = new ArrayList<>();
        staticObstacleList.add(staticObst);
        staticObstacleList.add(staticObst2);

        // End Configuration //

        RRT rrt = new RRT(testBox, goal, boxList, staticObstacleList);
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
