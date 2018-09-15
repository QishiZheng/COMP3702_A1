package solver;

import problem.Box;
import problem.ProblemSpec;
import problem.RobotConfig;
import problem.StaticObstacle;
import tester.Tester;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
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
        RobotConfig r2 = new RobotConfig(new Point2D.Double(0.1, 0.1), 0.02);
        System.out.println("Goal robotConf: (" +r2.getPos().getX() + ", "
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
        List<RobotConfig> path = prm.BFS(sg);
        if(path != null) {
            for(int i = 0; i < path.size(); i++) {
                RobotConfig r = path.get(i);
                System.out.println(i + " RobotConf: (" +r.getPos().getX() + ", "
                        + r.getPos().getY() + ", " + r.getOrientation() + ")");
                RRT rrt = new RRT(s.getBoxes().get(0), r.getPos(), s.getMovingObst(), s.getStaticObstSt());
                try {
                    writeResult(rrt, r);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
     * Writes a solution to a file
     * @param rrt the RRT to get data from
     * @param r the RobotConfig with the robot's location
     * @throws IOException if there is an error with the file
     */
    private static void writeResult(RRT rrt, RobotConfig r) throws IOException {
        File f = new File("resultoutput.txt");

        BufferedWriter writer = new BufferedWriter(new FileWriter(f));

        // write the amount of primitive steps on the path
        writer.write(Integer.toString(rrt.getCoordPath().size()));
        writer.newLine();

        // write the locations of the robot, boxes and obstacles
        for (Point2D coord : rrt.getCoordPath()) {
            // write the robot's configuation
            writer.write(Double.toString(coord.getX()) + " ");
            writer.write(Double.toString(coord.getY()) + " ");
            writer.write(Double.toString(r.getOrientation()));

            // write the boxes' coordinates
            for (Box box : rrt.getBoxes()) {
                writer.write(" " + box.getPos().getX());
                writer.write(" " + box.getPos().getY());
            }
            writer.newLine();
        }
        writer.close();
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

    /** For debugging */
    static private void p(Object o) {
        System.out.println(o.toString());
    }



}
