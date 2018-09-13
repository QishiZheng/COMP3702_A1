package solver;

import problem.Box;
import problem.ProblemSpec;
import problem.RobotConfig;
import tester.Tester;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

/**
 * PRM algorithm for sampling configuration of the robot
 */
public class PRM {
    //max number of nodes to put in the roadmap
    private int n;
    //max number of closest neighbors to exam for each config
    private int k;
    private RobotConfig init;
    private RobotConfig goal;

    private Tester ts;
    private State states;
    private ProblemSpec ps;

    //constructs PRM and load problem spec
    public PRM(ProblemSpec problem) {
        this.ps = problem;

        states = new State(problem);
        this.ts = new Tester(problem);
    }

    //Constructs with maxnode and maxNeighbors
    //TODO: TO BE FULLY IMPLEMENTED
    public PRM(ProblemSpec problem, State s, int maxNode, int maxNeighbor, RobotConfig init, RobotConfig goal) {
        this.ps = problem;
        this.n = maxNode;
        this.k = maxNeighbor;
        this.init = init;
        this.goal = goal;

        this.states = s;
        this.ts = new Tester(problem);
    }



    /**
     * Build state graph for robot config
     * @return a graph that contains path from init state to goal state
     */
    public StateGraph<RobotConfig> buildGraph() {
        //set init state vertex
        Vertex<RobotConfig> initVertex = new Vertex<>(this.init);
        //set goal vertex
        Vertex<RobotConfig> goalVertex = new Vertex<>(this.goal);
        //initialise teh stateGraph with init and goal vertex
        StateGraph<RobotConfig> roadmap = new StateGraph<>(initVertex, goalVertex);

        //sample n random robotConfig
        while(roadmap.numOfVertex() - 2 < this.n) {
            RobotConfig sample = randomRobotConfig();
            //check if this sample config collides with any obstacles,
            //Add the sample to roadmap/graph if no collision
            if(!(this.states.robotCollision(sample))) {
                roadmap.addVertex(new Vertex<>(sample));
                //System.out.println("POS: (" + sample.getPos().getX() + ", " + sample.getPos().getY() + ")\n");
            }
        }

        //System.out.println("SIZE: " + roadmap.numOfVertex() + "\n");

        //TODO: Connect the vertices with edges

        return roadmap;
    }

//    /**
//     * Check if given robot config has collision with all of the movable objects
//     * in the giving list of boxes
//     * @param rc robot config
//     * @param movables a list of movable objects, include moving boxes and movable obstacles
//     * @return true if has collision
//     */
//    private boolean hasCollision(RobotConfig rc, List<Box> movables) {
//        return ts.hasCollision(rc, movables);
//    }

    /**
     * Sample an random robot config uniformly
     * @return an random robot config
     *
     */
    private RobotConfig randomRobotConfig() {
        Point2D p = randomPoint();
        Double angle = randomAngle();
        return new RobotConfig(p, angle);
    }


    /**
     * Generate an random point with coords from 0 to 1
     * @return an random point with coords from 0 to 1 inclusively, 0.001 unit
     */
    private Point2D randomPoint() {
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
    private Double randomAngle() {
        Double rad = (Double)((new Random().nextInt(1001))/1000.0);
        return rad * Math.PI;
    }



    //For testing methods
    public static void main(String[] args) {

    }

}
