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

//        try {
//            ps.loadProblem(args[0]);
//        } catch (IOException e1) {
//            System.out.println("FAILED: Invalid problem file");
//            System.out.println(e1.getMessage());
//            return;
//        }


        try {
            ps.loadProblem("input2.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create a initial state from the problrmSpec
        State s = new State(ps);
        List<State> path = new LinkedList<>();
        //the length of robot
        Double robotLength = ps.getRobotWidth();
        //get the initial and goal robot config from problemSpec
        RobotConfig robotInit = ps.getInitialRobotConfig();

        //GET THE PAHT OF MOVINGBOXES states
        Astar astar = new Astar(ps);
        List<LinkedList<State>> boxStates= astar.getStateList();

        for(LinkedList<State> ls : boxStates) {
            ls.getLast().setProblemSpec(ps);
        }

        RobotConfig robotGoal = boxStates.get(0).getFirst().getRobot();
        System.out.println(robotInit);
        System.out.println(robotGoal);

        PRM prm = new PRM(ps, s, 500, 10, robotInit, robotGoal);
        HashMap<RobotConfig, Set<RobotConfig>> sg = prm.buildMap();

        //check if the path exists
        if(prm.BFS(sg, robotInit,robotGoal) != null) {
            path = prm.BFS(sg,robotInit,robotGoal);
        } else {
            System.out.println("No Solution from init to 1st box");
        }

        // the state list of boxes
        if (boxStates.size()  == 1 ) {
            path.addAll(boxStates.get(0));
        } else {
            for (int i = 0; i < boxStates.size() -1 ; i++) {
                //add state path of ith box
                path.addAll(boxStates.get(i));

//            //calculate the path from the box to next
//            robotInit = boxStates.get(i).getLast().getRobot();
//            robotGoal = boxStates.get(i + 1).getFirst().getRobot();
//            System.out.println(robotInit);
//            System.out.println(robotGoal);
                PRM prm2 = new PRM(ps, boxStates.get(i).getLast(), 500, 10, boxStates.get(i).getLast().getRobot(), boxStates.get(i+1).getFirst().getRobot());
                HashMap<RobotConfig, Set<RobotConfig>> roadmap = prm2.buildMap();

                //check if the path exists
                if(prm2.BFS(roadmap,  boxStates.get(i).getLast().getRobot(), boxStates.get(i+1).getFirst().getRobot()) != null) {
                    path.addAll(prm2.BFS(roadmap, boxStates.get(i).getLast().getRobot(), boxStates.get(i+1).getFirst().getRobot()));
                    path.addAll(boxStates.get(i+1));
                } else {
                    System.out.println("No Solution from " + i + "th to"+ i+1+"th box");
                }
            }

        }

        SolutionWriter writer = new SolutionWriter();
        try {
            writer.writePath(path, "solution1.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            writer.writePath(path, args[1]);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }









////        prm.setInit(robotInit);
////        prm.setGoal(robotGoal);
//
//            //StateGraph sg = prm.buildGraph();
//            HashMap<RobotConfig, Set<RobotConfig>> sg = prm.buildMap();








//        //TODO: TO BE REMOVED LATER,  KEEPING IT NOW ONLY FOR TESTING PURPOSE
//        //get the initial robot config from problemSpec
//        //RobotConfig robotInit = ps.getInitialRobotConfig();
//        System.out.println("Root robotConf: (" +robotInit.getPos().getX() + ", "
//                + robotInit.getPos().getY() + ", " + robotInit.getOrientation() + ")");
//
//
//        RobotConfig robotGoal = new RobotConfig(new Point2D.Double(ps.getMovingBoxes().get(1).getPos().getX() + robotLength, ps.getMovingBoxes().get(1).getPos().getY()  + robotLength/2), Math.PI/2);
//        if(!s.robotCollisionFree(robotGoal)) {
//            System.out.println("Goal in obstacle\n" + "***********************");
//        }
//
//        System.out.println("Goal robotConf: (" +robotGoal.getPos().getX() + ", "
//                + robotGoal.getPos().getY() + ", " + robotGoal.getOrientation() + ")");
//
//        PRM prm = new PRM(ps, s, 500, 10, robotInit, robotGoal);
////        prm.setInit(robotInit);
////        prm.setGoal(robotGoal);
//
//        //StateGraph sg = prm.buildGraph();
//        HashMap<RobotConfig, Set<RobotConfig>> sg = prm.buildMap();
////        System.out.println("#Vertex: "+sg.numOfVertex());
//        System.out.println("#Vertex: "+sg.keySet().size());
//
//
//        long consTime = System.currentTimeMillis();
//        System.out.println("Graph Construction Took "+(consTime - startTime)/1000 + " s");
//
//        System.out.println("Num of edges that Root vertex has: " + sg.get(prm.getInit()).size());
//        System.out.println("Num of edges that Goal vertex has: " + sg.get(prm.getGoal()).size());
//
//        //check if the path exists
//        if(prm.BFS(sg, robotInit,robotGoal) != null) {
//            System.out.println("Path for robotGoal:\n");
//            path = prm.BFS(sg,robotInit,robotGoal);
//
//            //time taken
//            long searchTime = System.currentTimeMillis();
//            System.out.println("Search Time Took "+(searchTime - consTime) + " ms");
//
////            for(int i = 0; i < path.size(); i++) {
////            State state = path.get(i);
////            System.out.println(i + " State: (" + state.toString() + ")");
////            }
//
//            SolutionWriter writer = new SolutionWriter();
//            try {
//                writer.writePath(path, "solution1.txt");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            long writeTime = System.currentTimeMillis();
//            System.out.println("Writing Time Took "+(writeTime - searchTime) + " ms");
//
//        } else {
//            System.out.println("No Solution");
//        }






        /**
         * Time taken for running the program
         * */
        long endTime = System.currentTimeMillis();
        System.out.println("Total Time Took "+(endTime - startTime)/1000 + " s");
    }

}
