package solver;

import problem.ProblemSpec;
import problem.RobotConfig;
import tester.Tester;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The search agent to solve the motion planning problem
 */
public class Agent {
    private ProblemSpec ps;

    public void agent(ProblemSpec problem) {
        this.ps = problem;
    }

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        ProblemSpec ps = new ProblemSpec();
        try {
            ps.loadProblem("input1.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create a initial state from the problrmSpec
        State s = new State(ps);
        //the length of robot
        Double robotLength = ps.getRobotWidth();


        //get the initial robot config from problemSpec
        RobotConfig robotInit = ps.getInitialRobotConfig();
        System.out.println("Root robotConf: (" +robotInit.getPos().getX() + ", "
                + robotInit.getPos().getY() + ", " + robotInit.getOrientation() + ")");

        RobotConfig robotGoal = new RobotConfig(new Point2D.Double(0.6, 0.4), 0);
        System.out.println("Goal robotConf: (" +robotGoal.getPos().getX() + ", "
                + robotGoal.getPos().getY() + ", " + robotGoal.getOrientation() + ")");

        PRM prm = new PRM(ps, s, 2000, 30, robotInit, robotGoal);

        StateGraph sg = prm.buildGraph();
        System.out.println("#Vertex: "+sg.numOfVertex());

        long consTime = System.currentTimeMillis();
        System.out.println("Graph Construction Took "+(consTime - startTime)/1000 + " s");
        //print out all vertices
//        List<Vertex<RobotConfig>> vertices = new ArrayList<>(sg.getAllVertices());
//        for(int i = 0; i < vertices.size(); i++) {
//            System.out.println(vertices.get(i).toString() + "has #neighbors: " + vertices.get(i).getNumOfNeighbors());
//        }

        System.out.println("Num of edges that Root vertex has: " + sg.getRootVertex().getNumOfNeighbors());
        System.out.println("Num of edges that Goal vertex has: " + sg.getGoalVertex().getNumOfNeighbors());


        //check if the path exists
        if(prm.BFS(sg, robotInit,robotGoal) != null) {
            List<RobotConfig> path = prm.BFS(sg,robotInit,robotGoal);
            for(int i = 0; i < path.size(); i++) {
            RobotConfig r = path.get(i);
            System.out.println(i + " RobotConf: (" +r.getPos().getX() + ", "
                    + r.getPos().getY() + ", " + r.getOrientation() + ")");
            }
        } else {
            System.out.println("No Solution");
        }

        //time taken
        long searchTime = System.currentTimeMillis();
        System.out.println("Search Time Took "+(searchTime - consTime) + " ms");

        long endTime = System.currentTimeMillis();
        System.out.println("Total Time Took "+(endTime - startTime)/1000 + " s");

        //List<RobotConfig> path = prm.searchPath(sg);

//        for(int i = 0; i < path.size(); i++) {
//            RobotConfig r = path.get(i);
//            System.out.println(i + " RobotConf: (" +r.getPos().getX() + ", "
//                    + r.getPos().getY() + ", " + r.getOrientation() + ")");
//        }

    }


    /**
     * Sample an random robot config uniformly
     * @return an random robot config
     *
     */
    private static RobotConfig randomRobotConfig() {
        Point2D p = randomPoint();
        Double angle = randomAngle();
        return new RobotConfig(p, angle);
    }


    /**
     * Generate an random point with coords from 0 to 1
     * @return an random point with coords from 0 to 1 inclusively, 0.001 unit
     */
    private static Point2D randomPoint() {
        int xInt = new Random().nextInt(1000);
        int yInt = new Random().nextInt(1000);
        Double x = (Double)(xInt/1000.0);
        Double y = (Double)(yInt/1000.0);
        return new Point2D.Double(x, y);
    }

    /**
     * Generate an random angle range from 0 - pi
     * @return an random angle range from 0 - pi inclusively, 0.001 unit
     * TODO: MIGHT BE WRONG TO SAMPLE ANGLE LIKE THIS
     */
    private static Double randomAngle() {
        int radInt = new Random().nextInt((int)(1000 * Math.PI));
        Double rad = (Double)(radInt/1000.0);

//        Double rad = new Random().nextDouble();
        rad = (double)Math.round(rad * 1000d) / 1000d;
        return rad;
    }


    /**
     * Get the path from point a to point b at maximum of unit 0.001 per step
     * @param a point a
     * @param b point b
     * @return a list of Point2D the path from point a to point b
     */
//    private List<Point2D> movingBoxPathBreakdown(Point2D a, Point2D b) {
//
//    }



}
