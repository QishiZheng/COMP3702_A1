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
 * The search agent
 */
public class Agent {
    private ProblemSpec ps;

    public void agent(ProblemSpec problem) {
        ps = problem;
    }

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        ProblemSpec ps = new ProblemSpec();
        try {
            ps.loadProblem("input1.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        State s = new State(ps);
        RobotConfig r1 = new RobotConfig(new Point2D.Double(0.7, 0.7), 0.02);
        System.out.println("Root robotConf: (" +r1.getPos().getX() + ", "
                + r1.getPos().getY() + ", " + r1.getOrientation() + ")");
        RobotConfig r2 = new RobotConfig(new Point2D.Double(0.1, 0.1), 0.02);        System.out.println("Goal robotConf: (" +r2.getPos().getX() + ", "
                + r2.getPos().getY() + ", " + r2.getOrientation() + ")");
        PRM prm = new PRM(ps, s, 2500, 25, r1, r2);
        StateGraph sg = prm.buildGraph();
        System.out.println("#Vertex: "+sg.numOfVertex());

        //print out all vertices
//        List<Vertex<RobotConfig>> vertices = new ArrayList<>(sg.getAllVertices());
//        for(int i = 0; i < vertices.size(); i++) {
//            System.out.println(vertices.get(i).toString() + "has #neighbors: " + vertices.get(i).getNumOfNeighbors());
//        }

        System.out.println("Num of edges that Root vertex has: " + sg.getRootVertex().getNumOfNeighbors());
        System.out.println("Num of edges that Goal vertex has: " + sg.getGoalVertex().getNumOfNeighbors());


        //check if the path exists
        if(prm.BFS(sg) != null) {
            List<RobotConfig> path = prm.BFS(sg);
            for(int i = 0; i < path.size(); i++) {
            RobotConfig r = path.get(i);
            System.out.println(i + " RobotConf: (" +r.getPos().getX() + ", "
                    + r.getPos().getY() + ", " + r.getOrientation() + ")");
            }
        } else {
            System.out.println("No Solution");
        }

        //time taken
        long endTime = System.currentTimeMillis();
        System.out.println("Took "+(endTime - startTime)/1000 + " s");

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
        Double rad = (Double)((new Random().nextInt(1001))/1000.0);
        return rad * Math.PI;
    }






}
