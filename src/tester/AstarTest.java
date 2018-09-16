package tester;

import problem.*;
import solver.Astar;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AstarTest {
    public static void main(String[] args) {
        // Configure here //
        ProblemSpec ps = new ProblemSpec();
        try {
            ps.loadProblem("input1.txt");
        } catch (IOException ioe) {
            System.out.println("FAILED: Invalid problem file");
            System.out.println(ioe.getMessage());
        }
        // End Configuration //

        Astar astar = new Astar(ps);
        List<LinkedList<Point2D>> listOfPaths = astar.getMvBoxPaths();
        List<LinkedList<Point2D>> listOfObstPaths = astar.getMvObstPaths();

        int j = 0;
        for (LinkedList<Point2D> path : listOfPaths) {
            if (path != null) {
                System.out.println("BOX PATH:");
                for (Point2D coord : path) {
                    System.out.println("X: " + coord.getX() + ", Y: " + coord.getY());
                }
            } else {
                for (int i = 0; i < ps.getMovingObstacles().size(); i++, j++) {
                    System.out.println("OBSTACLE PATH:");
                    if (listOfObstPaths.get(j) != null) {
                        for (Point2D coord : listOfObstPaths.get(j)) {
                            System.out.println("X: " + coord.getX() + ", Y: " + coord.getY());
                        }
                    } else {
                        System.out.println("null");
                    }
                }
            }
        }
    }
}
