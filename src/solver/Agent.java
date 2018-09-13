package solver;

import problem.ProblemSpec;
import problem.RobotConfig;
import tester.Tester;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Random;

/**
 * The search agent
 */
public class Agent {
    private ProblemSpec ps;

    public void agent(ProblemSpec problem) {
        ps = problem;
    }

//    public static void main(String[] args) {
//        ProblemSpec ps = new ProblemSpec();
//        try {
//            ps.loadProblem("input1.txt");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        State s = new State(ps);
//        RobotConfig r1 = randomRobotConfig();
//        RobotConfig r2 = randomRobotConfig();
//        PRM prm = new PRM(ps, s, 1000, 20, r1, r2);
//        StateGraph sg = prm.buildGraph();
//        //System.out.println("#Vertex: "+sg.numOfVertex());
//
//
//    }
//
//
//    /**
//     * Sample an random robot config uniformly
//     * @return an random robot config
//     *
//     */
//    private static RobotConfig randomRobotConfig() {
//        Point2D p = randomPoint();
//        Double angle = randomAngle();
//        return new RobotConfig(p, angle);
//    }
//
//
//    /**
//     * Generate an random point with coords from 0 to 1
//     * @return an random point with coords from 0 to 1 inclusively, 0.001 unit
//     */
//    private static Point2D randomPoint() {
//        int xInt = new Random().nextInt(1000);
//        int yInt = new Random().nextInt(1000);
//        Double x = (Double)(xInt/1000.0);
//        Double y = (Double)(yInt/1000.0);
//        return new Point2D.Double(x, y);
//    }
//
//    /**
//     * Generate an random angle range from 0 - pi
//     * @return an random angle range from 0 - pi inclusively, 0.001 unit
//     * TODO: MIGHT BE WRONG TO SAMPLE ANGLE LIKE THIS
//     */
//    private static Double randomAngle() {
//        Double rad = (Double)((new Random().nextInt(1001))/1000.0);
//        return rad * Math.PI;
//    }






}
