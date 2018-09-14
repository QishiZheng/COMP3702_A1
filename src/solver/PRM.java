package solver;

import problem.Box;
import problem.ProblemSpec;
import problem.RobotConfig;
import tester.Tester;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

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
        //initialise the stateGraph with init and goal vertex
        StateGraph<RobotConfig> roadmap = new StateGraph<RobotConfig>(initVertex, goalVertex);

        //sample n random robotConfig
        while(roadmap.numOfVertex() - 2 < this.n) {
            RobotConfig sample = randomRobotConfig();
            //check if this sample config collides with any obstacles,
            //Add the sample to roadMap/graph if no collision
            if(!(this.states.robotCollisionFree(sample))) {
                roadmap.addVertex(new Vertex<RobotConfig>(sample));
                //System.out.println("POS: (" + sample.getPos().getX() + ", " + sample.getPos().getY() + ")\n");
            }
        }

        //System.out.println("SIZE: " + roadmap.numOfVertex() + "\n");

        //TODO: Get the k nearest reachable neighbor vertices
        for(Vertex<RobotConfig> vr1 : roadmap.getAllVertices()) {
            RobotComparator comp = new RobotComparator();
            comp.setRobot(vr1.getState());
            //PriorityQueue for holding the k closest neighbor RobotConfigs
            PriorityQueue<RobotConfig> pq = new PriorityQueue<RobotConfig>(k, comp);

            //list of k nearest neighbors for vr1
            List<Vertex<RobotConfig>> neighbors = new ArrayList<Vertex<RobotConfig>>();
            for(Vertex<RobotConfig> vr2 : roadmap.getAllVertices()) {
                //no need to set the vertex itself as its neighbor
                if (vr2.getState().equals(vr1.getState())) { continue; }
                //check if this neighbor vertex is reachable:
                //  Sample multiple points between start and end vertex,
                //  then check if each point is collision free

                //if the neighbor vertex is reachable, add it to pq
                if(isPathCollisionFree(vr2.getState(), vr1.getState())) {
                    pq.add(vr2.getState());
                }
            }

            //added k nearest reachable neighbor to neighbor list
            for (int i = 0; i < k; ++i) {
                if(pq.peek() != null) {
                    neighbors.add(new Vertex<RobotConfig>(pq.poll()));
                }
            }

            //add edges of this vertex to roadmap
            for(int i = 0; i < neighbors.size(); i++) {
                roadmap.addEdge(new Edge<>(vr1, neighbors.get(i)));
            }
        }

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

    /**
     * Check if the path between RobotConfig is collision free
     * @param r1 RobotConfig 1
     * @param r2 RobotConfig 1
     * @return true if the path is collision free
     */
    private boolean isPathCollisionFree(RobotConfig r1, RobotConfig r2) {
        List<RobotConfig> samples = new ArrayList<>(sampleOnEdge(r1, r2, 10));
        for(RobotConfig r : samples) {
            if(!this.states.robotCollisionFree(r)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sample n RobotConfigs between RobotConfig r1 and RobotConfig r2
     * @param r1 RobotConfig 1
     * @param r2 RobotConfig 2
     * @param n number of samples
     * @return A list of n RobotConfigs
     */
    private List<RobotConfig> sampleOnEdge(RobotConfig r1, RobotConfig r2, int n) {
        List<RobotConfig> samples = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            Double x = r1.getPos().getX() + (i * ((Math.abs(r1.getPos().getX() - r2.getPos().getX())) / n));
            Double y = r1.getPos().getY() + (i * ((Math.abs(r1.getPos().getY() - r2.getPos().getY())) / n));
            Double angle = r1.getPos().getY() + (i * ((Math.abs(r1.getOrientation() - r2.getOrientation())) / n));
            RobotConfig s  = new RobotConfig(new Point2D.Double(x, y), angle);
            samples.add(s);
        }
        return samples;
    }


    //For testing methods
    public static void main(String[] args) {

    }

}
