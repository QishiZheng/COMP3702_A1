package solver;

import problem.ProblemSpec;
import problem.RobotConfig;
import tester.Tester;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;

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
            ps.loadProblem("input2.txt");
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

        RobotConfig robotGoal = new RobotConfig(new Point2D.Double(0.11, 0.11), Math.PI/2);
        if(!s.robotCollisionFree(robotGoal)) {
            System.out.println("Goal in obstacle\n" + "***********************");
        }

        System.out.println("Goal robotConf: (" +robotGoal.getPos().getX() + ", "
                + robotGoal.getPos().getY() + ", " + robotGoal.getOrientation() + ")");

//        RobotConfig robotGoal2 = new RobotConfig(new Point2D.Double(0.1, 0.7), 0);
//        if(!s.robotCollisionFree(robotGoal2)) {
//            System.out.println("Goal2 in obstacle\n" + "***********************");
//        }
//        System.out.println("Goal2 robotConf: (" +robotGoal2.getPos().getX() + ", "
//                + robotGoal2.getPos().getY() + ", " + robotGoal2.getOrientation() + ")");

        PRM prm = new PRM(ps, s, 1000, 10, robotInit, robotGoal);

        //StateGraph sg = prm.buildGraph();
        HashMap<RobotConfig, Set<RobotConfig>> sg = prm.buildMap();
//        System.out.println("#Vertex: "+sg.numOfVertex());
        System.out.println("#Vertex: "+sg.keySet().size());


        long consTime = System.currentTimeMillis();
        System.out.println("Graph Construction Took "+(consTime - startTime)/1000 + " s");
        //print out all vertices
//        List<Vertex<RobotConfig>> vertices = new ArrayList<>(sg.getAllVertices());
//        for(int i = 0; i < vertices.size(); i++) {
//            System.out.println(vertices.get(i).toString() + "has #neighbors: " + vertices.get(i).getNumOfNeighbors());
//        }

//        System.out.println("Num of edges that Root vertex has: " + sg.getRootVertex().getNumOfNeighbors());
//        System.out.println("Num of edges that Goal vertex has: " + sg.getGoalVertex().getNumOfNeighbors());
        System.out.println("Num of edges that Root vertex has: " + sg.get(prm.getInit()).size());
        System.out.println("Num of edges that Goal vertex has: " + sg.get(prm.getGoal()).size());

        //check if the path exists
        if(prm.BFS(sg, robotInit,robotGoal) != null) {
            System.out.println("Path for robotGoal:\n");
            List<RobotConfig> path = prm.BFS(sg,robotInit,robotGoal);
            for(int i = 0; i < path.size(); i++) {
            RobotConfig r = path.get(i);
            System.out.println(i + " RobotConf: (" +r.getPos().getX() + ", "
                    + r.getPos().getY() + ", " + r.getOrientation() + ")");
            }
        } else {
            System.out.println("No Solution");
        }

//        //check if the path exists for goal 2
//        prm.addNodeToGraph(sg, robotGoal2);
//        if(prm.BFS(sg, robotInit,robotGoal2) != null) {
//            System.out.println("Path for robotGoal2:\n");
//            List<RobotConfig> path = prm.BFS(sg,robotInit,robotGoal2);
//            for(int i = 0; i < path.size(); i++) {
//                RobotConfig r = path.get(i);
//                System.out.println(i + " RobotConf: (" +r.getPos().getX() + ", "
//                        + r.getPos().getY() + ", " + r.getOrientation() + ")");
//            }
//        } else {
//            System.out.println("Goal 2 No Solution");
//        }

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
     * Get the path from point a to point b at maximum of unit 0.001 per step
     * @param a point a
     * @param b point b
     * @return a list of Point2D the path from point a to point b
     */
//    private List<Point2D> movingBoxPathBreakdown(Point2D a, Point2D b) {
//
//    }



}
