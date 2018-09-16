package solver;

import problem.Box;
import problem.ProblemSpec;
import problem.RobotConfig;
import tester.Tester;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicStampedReference;

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


        //TODO: GET THE PAHT OF MOVINGBOXES
//        Astar astar = new Astar();






        //TODO: TO BE REMOVED LATER,  KEEPING IT NOW ONLY FOR TESTING PURPOSE
        //get the initial robot config from problemSpec
        RobotConfig robotInit = ps.getInitialRobotConfig();
        System.out.println("Root robotConf: (" +robotInit.getPos().getX() + ", "
                + robotInit.getPos().getY() + ", " + robotInit.getOrientation() + ")");

        RobotConfig robotGoal = new RobotConfig(new Point2D.Double(0.666, 0.875), Math.PI/2);
        if(!s.robotCollisionFree(robotGoal)) {
            System.out.println("Goal in obstacle\n" + "***********************");
        }

        System.out.println("Goal robotConf: (" +robotGoal.getPos().getX() + ", "
                + robotGoal.getPos().getY() + ", " + robotGoal.getOrientation() + ")");

        PRM prm = new PRM(ps, s, 500, 10);
        prm.setInit(robotInit);
        prm.setGoal(robotGoal);

        //StateGraph sg = prm.buildGraph();
        HashMap<RobotConfig, Set<RobotConfig>> sg = prm.buildMap();
//        System.out.println("#Vertex: "+sg.numOfVertex());
        System.out.println("#Vertex: "+sg.keySet().size());


        long consTime = System.currentTimeMillis();
        System.out.println("Graph Construction Took "+(consTime - startTime)/1000 + " s");

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

        //time taken
        long searchTime = System.currentTimeMillis();
        System.out.println("Search Time Took "+(searchTime - consTime) + " ms");





        /**
         * Time taken for running the program
         * */
        long endTime = System.currentTimeMillis();
        System.out.println("Total Time Took "+(endTime - startTime)/1000 + " s");
    }

}
