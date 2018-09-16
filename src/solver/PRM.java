package solver;

import problem.Box;
import problem.ProblemSpec;
import problem.RobotConfig;
import tester.Tester;

import javax.sound.sampled.Line;
import java.awt.*;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * PRM algorithm for sampling configuration of the robot.
 * CONSTRUCT A NEW PRM OBJECT EVERY TIME WHEN WE WANT TO DO SEARCH IN A NEW STATE
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

//    //Constructs with maxnode, maxNeighbors, init and goal config
//    //TODO: TO BE FULLY IMPLEMENTED
//    public PRM(ProblemSpec problem, State s, int maxNode, int maxNeighbor, RobotConfig init, RobotConfig goal) {
//        this.ps = problem;
//        this.n = maxNode;
//        this.k = maxNeighbor;
//        this.init = init;
//        this.goal = goal;
//
//        this.states = s;
//        this.ts = new Tester(problem);
//    }

    /**
     * Constructor
     * @param problem the problem spec we are dealing with
     * @param initState init state to start
     * @param maxNode
     * @param maxNeighbor
     * @param init initial robotConfig
     * @param goal goal robotConfig
     */
    public PRM(ProblemSpec problem, State initState, int maxNode, int maxNeighbor, RobotConfig init, RobotConfig goal) {
        this.ps = problem;
        this.n = maxNode;
        this.k = maxNeighbor;
        this.setInit(init);
        this.setGoal(goal);

        this.states = initState;
        this.ts = new Tester(problem);
    }


    /**
     * get init
     */
    public RobotConfig getInit() {
        return this.init;
    }

    public RobotConfig getGoal() {
        return this.goal;
    }

    /**
     * set the init PRM to given robotConf
     * @param rc
     */
    public void setInit(RobotConfig rc) {
        this.init = rc;
    }

    /**
     * set the goal of PRM to given robotConf
     * @param rc
     */
    public void setGoal(RobotConfig rc) {
        this.goal = rc;
    }


    /**
     * Build the graph for robotConfig with fixed collision region.
     * @return a graph of robotConfig in the form of map
     */
    public HashMap<RobotConfig, Set<RobotConfig>> buildMap() {
        // Hashmap to hold the vertices and their k neighbors
        HashMap<RobotConfig, Set<RobotConfig>> roadmap = new HashMap<RobotConfig, Set<RobotConfig>>();
        // Initialize start and end RobotConfigs

        // Add in start and end RobotConfigs to the roadmap
        roadmap.put(this.init, null);
        roadmap.put(this.goal, null);
        // Generate N random configurations

        //sample n random robotConfig
        while(roadmap.size() - 2 < this.n) {
            RobotConfig sample = randomRobotConfig();
            //check if this sample config collides with any obstacles,
            //Add the sample to roadMap/graph if no collision
            if(this.states.robotCollisionFree(sample)) {
                roadmap.put(sample, null);
            }
        }

        // Getting the k nearest neighbors
        for (RobotConfig rc1 : roadmap.keySet()) {
           newconnectKthNearestNeighbors(roadmap, rc1);
        }
        return roadmap;
    }


    /**
     * Get the k nearest reachable neighbor vertices and connect them
     * @param roadmap given roadmap
     */
    private void newconnectKthNearestNeighbors(HashMap<RobotConfig, Set<RobotConfig>> roadmap, RobotConfig rc1) {
        RobotComparator comp = new RobotComparator();
        comp.setRobot(rc1);
        //PriorityQueue for holding the k closest neighbor RobotConfigs
        PriorityQueue<RobotConfig> pq = new PriorityQueue<RobotConfig>(k, comp);

        //set of k nearest neighbors for rc1
        Set<RobotConfig> neighbors = new HashSet<>();
        for(RobotConfig rc2 : roadmap.keySet()) {
            //no need to set the vertex itself as its neighbor
            if (rc2.equals(rc1)) { continue; }

            //if the neighbor vertex is reachable, add it to pq
            if(isPathCollisionFree(rc1, rc2)) {
                pq.add(rc2);
            }
        }

        //added k nearest reachable neighbor to neighbor set
        for (int i = 0; i < k; ++i) {
            if(pq.peek() != null) {
                neighbors.add(pq.poll());
            }
        }

        //add edges of this vertex to roadmap
        roadmap.put(rc1, neighbors);
    }

    /**
     * add node to graph and connect nearest neighbors
     * @param roadmap the roadmap that the node will be added to
     * @param rc the robotConfig to be added
     */
    public void addNodeToGraph(HashMap<RobotConfig, Set<RobotConfig>> roadmap, RobotConfig rc) {
        if(this.states.robotCollisionFree(rc)) {
            roadmap.put(rc, null);
        }

        newconnectKthNearestNeighbors(roadmap, rc);
    }


//    /**
//     * Build state graph for robot config
//     * @return a graph that contains path from init state to goal state
//     */
//    public StateGraph<RobotConfig> buildGraph() {
//        //set init state vertex
//        Vertex<RobotConfig> initVertex = new Vertex<>(this.init);
//        //set goal vertex
//        Vertex<RobotConfig> goalVertex = new Vertex<>(this.goal);
//        //initialise the stateGraph with init and goal vertex
//        StateGraph<RobotConfig> roadmap = new StateGraph<RobotConfig>(initVertex, goalVertex);
//
//        //sample n random robotConfig
//        while(roadmap.numOfVertex() - 2 < this.n) {
//            RobotConfig sample = randomRobotConfig();
//            //check if this sample config collides with any obstacles,
//            //Add the sample to roadMap/graph if no collision
//            if(!(this.states.robotCollisionFree(sample))) {
//                roadmap.addVertex(new Vertex<RobotConfig>(sample));
//                //System.out.println("POS: (" + sample.getPos().getX() + ", " + sample.getPos().getY() + ")\n");
//            }
//        }
//
//        //System.out.println("SIZE: " + roadmap.numOfVertex() + "\n");
//
//        //Get the k nearest reachable neighbor vertices
//        for(Vertex<RobotConfig> vr1 : roadmap.getAllVertices()) {
//            connectKthNearestNeighbors(roadmap, vr1);
////            RobotComparator comp = new RobotComparator();
////            comp.setRobot(vr1.getState());
////            //PriorityQueue for holding the k closest neighbor RobotConfigs
////            PriorityQueue<RobotConfig> pq = new PriorityQueue<RobotConfig>(k, comp);
////
////            //list of k nearest neighbors for vr1
////            List<Vertex<RobotConfig>> neighbors = new ArrayList<Vertex<RobotConfig>>();
////            for(Vertex<RobotConfig> vr2 : roadmap.getAllVertices()) {
////                //no need to set the vertex itself as its neighbor
////                if (vr2.getState().equals(vr1.getState())) { continue; }
////
////                //if the neighbor vertex is reachable, add it to pq
////                if(isPathCollisionFree(vr2.getState(), vr1.getState())) {
////                    pq.add(vr2.getState());
////                }
////            }
////
////            //added k nearest reachable neighbor to neighbor list
////            for (int i = 0; i < k; ++i) {
////                if(pq.peek() != null) {
////                    neighbors.add(new Vertex<RobotConfig>(pq.poll()));
////                }
////            }
////
////            //add edges of this vertex to roadmap
////            for(int i = 0; i < neighbors.size(); i++) {
////                roadmap.addEdge(new Edge<>(vr1, neighbors.get(i)));
////            }
//        }
//
//        return roadmap;
//    }
//
//
//    /**
//     * Get the k nearest reachable neighbor vertices and connect them
//     * @param roadmap given roadmap
//     */
//    private void connectKthNearestNeighbors(StateGraph<RobotConfig> roadmap, Vertex<RobotConfig> vr1) {
//            RobotComparator comp = new RobotComparator();
//            comp.setRobot(vr1.getState());
//            //PriorityQueue for holding the k closest neighbor RobotConfigs
//            PriorityQueue<RobotConfig> pq = new PriorityQueue<RobotConfig>(k, comp);
//
//            //list of k nearest neighbors for vr1
//            List<Vertex<RobotConfig>> neighbors = new ArrayList<Vertex<RobotConfig>>();
//            for(Vertex<RobotConfig> vr2 : roadmap.getAllVertices()) {
//                //no need to set the vertex itself as its neighbor
//                if (vr2.getState().equals(vr1.getState())) { continue; }
//
//                //if the neighbor vertex is reachable, add it to pq
//                if(isPathCollisionFree(vr2.getState(), vr1.getState())) {
//                    pq.add(vr2.getState());
//                }
//            }
//
//            //added k nearest reachable neighbor to neighbor list
//            for (int i = 0; i < k; ++i) {
//                if(pq.peek() != null) {
//                    neighbors.add(new Vertex<RobotConfig>(pq.poll()));
//                }
//            }
//
//            //add edges of this vertex to roadmap
//            for(int i = 0; i < neighbors.size(); i++) {
//                roadmap.addEdge(new Edge<>(vr1, neighbors.get(i)));
//            }
//
//    }
//
//    /**
//     * Find the optimised path in the given state graph of robotConfig
//     * @param sg given state graph
//     * @return a list of robotConfig in the order of moving path
//     */
//    public List<RobotConfig> searchPath(StateGraph<RobotConfig> sg) {
//        Astar<RobotConfig> searcher  = new Astar<RobotConfig>(sg);
////        List<RobotConfig> path = searcher.search(sg);
//        return searcher.search(sg);
//    }
//
//
//    //Breadth First Search in the graph of RobotConfig from init to goal
//    public List<RobotConfig> BFS(StateGraph<RobotConfig> roadmap, RobotConfig rootConf, RobotConfig goalConf) {
//        //Queue of Fringe
//        Queue<RobotConfig> fringe = new LinkedList<>();
//        //map to store backchain data
//        HashMap<RobotConfig, RobotConfig> reachedFrom = new HashMap<>();
//        //add root node
//        reachedFrom.put(this.init, null);
//        fringe.add(this.init);
//
//        while(!fringe.isEmpty()) {
//            //get the fist node
//            RobotConfig current = fringe.remove();
//            //check if this is the goal
//            if(current.equals(this.goal)) {
//                return backchainz(current, reachedFrom);
//            }
//            //get the neighbor nodes of this node
//            List<Vertex<RobotConfig>> succVertex = roadmap.getSuccessors(new Vertex(current));
//            List<RobotConfig> successors = new LinkedList<>();
//            for(Vertex<RobotConfig> v : succVertex) {
//                successors.add(v.getState());
//            }
//
//            //check each neighbors node
//            for (RobotConfig rc : successors) {
//                //added to reachedFrom if not explored
//                if(!reachedFrom.containsKey(rc)) {
//                    reachedFrom.put(rc, current);
//                    fringe.add(rc);
//                }
//            }
//        }
//        //return null if no solution found
//        return null;
//    }
//
//
//    //add this node to roadmap, and connect it to the nearest kth node in the map
//    public void addNodeAndConnectNeighbors(HashMap<RobotConfig, Set<RobotConfig>> roadmap, RobotConfig rc) {
//        roadmap.put(rc, null);
//        newconnectKthNearestNeighbors(roadmap, rc);
//    }


    /**
     * BFS search algorithm for searching path from root to goal in the given graph/roadmap
     * @param map given graph/roadmap
     * @param rootConf root robotConfig
     * @param goalConf goal robotConfig
     * @return linkedList of robotConfig
     */
    public LinkedList<State> BFS(HashMap<RobotConfig, Set<RobotConfig>> map, RobotConfig rootConf, RobotConfig goalConf) {
        // Fringe
        Queue<RobotConfig> fringe = new LinkedList<RobotConfig>();
        // Map to store backchain information
        HashMap<RobotConfig, RobotConfig> reachedFrom = new HashMap<RobotConfig, RobotConfig>();
        // Start node has no parent, add to fringe
        reachedFrom.put(rootConf, null);
        fringe.add(rootConf);
        while (!fringe.isEmpty()) {
            // Get node from fringe
            RobotConfig currentNode = fringe.remove();
            // Goal test
            if (currentNode.equals(goalConf)) {
//                return backchainz(currentNode, reachedFrom);
                LinkedList<RobotConfig> stepPath = new LinkedList<>(fullRobotPathBreakDown(backchainz(currentNode, reachedFrom)));
                LinkedList<State> stateStepPath = toStatePath(stepPath, this.states);
                return stateStepPath;
            }
            // Get the set of neighbors of the current node
            Set<RobotConfig> successors = map.get(currentNode);
            //Check each neighbor
            for (RobotConfig node : successors) {
                // If not visited
                if (!reachedFrom.containsKey(node)) {
                    reachedFrom.put(node, currentNode);
                    fringe.add(node);
                }
            }
        }
        //No solution
        return null;
    }
    
    
    private LinkedList<RobotConfig> backchainz(RobotConfig node,
                                              HashMap<RobotConfig, RobotConfig> parent) {
        LinkedList<RobotConfig> solution = new LinkedList<RobotConfig>();
        solution.addFirst(node);

        while (parent.get(node) != null) {
            solution.addFirst(parent.get(node));
            node = parent.get(node);
        }
        return solution;
    }


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
    private  Double randomAngle() {
        int radInt = new Random().nextInt((int)(1000 * Math.PI));
        Double rad = (Double)(radInt/1000.0);

//        Double rad = new Random().nextDouble();
        rad = (double)Math.round(rad * 1000d) / 1000d;
        return rad;
    }


//    /**
//     * Check if given robot config has collision with all of the movable objects
//     * in given state s
//     * @param rc robot config
//     * @param s given state for checking
//     * @return true if has no collision
//     *
//     */
//    private boolean robotCollisionFree(RobotConfig rc, State s) {
//        Tester ts = new Tester(this.ps);
//
//        List<Box> movables = new ArrayList<>();
//        movables.addAll(s.getBoxes());
//        movables.addAll(s.getMovingObst());
//        //System.out.println("Size of Obs: " + movables.size() + "\n");
//        return ts.hasCollision(rc, movables);
//    }


    /**
     * Convert the path of robot to path of state.
     * @param robotPath the robot path that we are going to convert
     * @param s the state before moving any boxes
     * @return a list of state
     */
    private LinkedList<State> toStatePath(List<RobotConfig> robotPath, State s) {
        LinkedList<State> statePath = new LinkedList<>();
        for(RobotConfig rc : robotPath) {
            statePath.add(new State(rc, s.getBoxes(), s.getMovingObst()));
        }
        return statePath;
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
    private static LinkedList<RobotConfig> sampleOnEdge(RobotConfig r1, RobotConfig r2, int n) {
        LinkedList<RobotConfig> samples = new LinkedList<>();

        Double x, y, angle;

        for(int i = 0; i < n; i++) {
            if(r1.getPos().getX() < r2.getPos().getX()) {
                x = r1.getPos().getX() + (i * ((Math.abs(r1.getPos().getX() - r2.getPos().getX())) / n));
            } else {
                x = r1.getPos().getX() - (i * ((Math.abs(r1.getPos().getX() - r2.getPos().getX())) / n));
            }

            if(r1.getPos().getY() < r2.getPos().getY()) {
                y = r1.getPos().getY() + (i * ((Math.abs(r1.getPos().getY() - r2.getPos().getY())) / n));
            } else {
                y = r1.getPos().getY() - (i * ((Math.abs(r1.getPos().getY() - r2.getPos().getY())) / n));
            }
            if(r1.getOrientation() < r2.getOrientation()) {
                angle = r1.getOrientation() + (i * ((Math.abs(r1.getOrientation() - r2.getOrientation())) / n));
            } else {
                angle = r1.getOrientation() - (i * ((Math.abs(r1.getOrientation() - r2.getOrientation())) / n));
            }

//            Double x = r1.getPos().getX() + (i * ((Math.abs(r1.getPos().getX() - r2.getPos().getX())) / n));
//            Double y = r1.getPos().getY() + (i * ((Math.abs(r1.getPos().getY() - r2.getPos().getY())) / n));
//            Double angle = r1.getOrientation() + (i * ((Math.abs(r1.getOrientation() - r2.getOrientation())) / n));
            RobotConfig s  = new RobotConfig(new Point2D.Double(doubleFormatter(x), doubleFormatter(y)), doubleFormatter(angle));
            samples.add(s);
        }
        return samples;
    }



    /**
     * Get the path from RobotConfig rc1 to RobotConfig rc2 at maximum of unit 0.001 per step
     * @param rc1 RobotConfig 1
     * @param rc2 RobotConfig 2
     * @return a list of robotConfig that represents path
     */
    private static LinkedList<RobotConfig> robotPathBreakDown(RobotConfig rc1, RobotConfig rc2) {
        LinkedList<RobotConfig> path = new LinkedList<>();
        Double distance = Math.sqrt(Math.pow(rc1.getPos().getX() - rc2.getPos().getX(), 2)
                + Math.pow(rc1.getPos().getY() - rc2.getPos().getY(), 2)
                + Math.pow(rc1.getOrientation() - rc2.getOrientation(), 2));

        int steps = stepsNeeded(rc1, rc2);
        path = sampleOnEdge(rc1, rc2, steps);
        return path;
    }

    //get the detailed step by step robot path from a rough path
    private static LinkedList<RobotConfig> fullRobotPathBreakDown(LinkedList<RobotConfig> path) {
        LinkedList<RobotConfig> fullPath = new LinkedList<>();
        for(int i = 0; i < path.size() - 1; i++) {
            fullPath.addAll(robotPathBreakDown(path.get(i), path.get(i + 1)));
        }
        return fullPath;
    }

    /**
     * Return the number of steps needed to move rc1 to rc2
     * each step cannot exceed 0.001
     * @param rc1
     * @param rc2
     * @return the number of steps needed to move rc1 to rc2
     */
    public static int stepsNeeded(RobotConfig rc1, RobotConfig rc2) {
        Double distance = Math.sqrt(Math.pow(rc1.getPos().getX() - rc2.getPos().getX(), 2)
                + Math.pow(rc1.getPos().getY() - rc2.getPos().getY(), 2)
                + Math.pow(rc1.getOrientation() - rc2.getOrientation(), 2));

        int steps = (int) Math.ceil(distance/0.001);
        return steps;
    }


    private static double doubleFormatter(Double num) {
        DecimalFormat formatter = new DecimalFormat("#0.000");
        return Double.parseDouble(formatter.format(num));
    }

    public static void main(String[] args) {
        RobotConfig rc1 = new RobotConfig(new Point2D.Double(0.1, 0.1), 0);
        RobotConfig rc2 = new RobotConfig(new Point2D.Double(0.3, 0.11), Math.PI/2);
        List<RobotConfig> path = robotPathBreakDown(rc1, rc2);
        for(RobotConfig r : path) {
            System.out.println(r);
        }
        System.out.println(stepsNeeded(rc1,rc2));
    }
}
