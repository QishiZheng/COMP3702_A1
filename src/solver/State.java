package solver;

import problem.*;
import tester.Tester;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A state of the motion planning problem.
 */
public class State {
    //state of robot
    private RobotConfig robotState;
    //state of boxes
    private List<Box> boxState;
    //state of moving obstacles
    private List<Box> movingObstState;
    //state of static obstacles
    private List<StaticObstacle> staticObstState;

    private ProblemSpec problemSpec;




    /**
     * Constructs a state from a ProblemSpec
     * @param ps Given ProblemSpec for constructing state
     */
    public State(ProblemSpec ps) {
        this.problemSpec = ps;
        this.robotState = ps.getInitialRobotConfig();
        this.boxState = ps.getMovingBoxes();
        this.movingObstState = ps.getMovingObstacles();
        //staticObstState = ps.getStaticObstacles();
    }


    /**
     * Constructs a state with RobotConfig, List of boxes and List of movingObstacles
     * @param rc RobotConfig
     * @param boxes a list of boxes
     * @param movingObst a list of moving obstacles
     */
    public State(RobotConfig rc, List<Box> boxes, List<Box> movingObst) {
        this.robotState = rc;
        this.boxState = boxes;
        this.movingObstState = movingObst;
    }

    /**
     * Get the robot config state
     * @return robot config state
     */
    public RobotConfig getRobot() {
        return this.robotState;
    }

    /**
     * Get the boxes state
     * @return a list of boxes
     */
    public List<Box> getBoxes() {
        return this.boxState;
    }

    /**
     * Get the moving obstacles state
     * @return a list of moving obstacles
     */
    public List<Box> getMovingObst() {
        return this.movingObstState;
    }

    /** Get the static obstacles state
     * @return a list of static obstacles
     */
    public List<StaticObstacle> getStaticObstSt() { return staticObstState; }


    /**
     * Check if given robot config has collision with all of the movable objects
     * in this problem spec
     * @param rc robot config
     * @return true if has no collision
     *
     */
    public boolean robotCollisionFree(RobotConfig rc) {
        Tester ts = new Tester(problemSpec);

        List<Box> movables = new ArrayList<>();
        movables.addAll(this.getBoxes());
        movables.addAll(this.getMovingObst());
        //System.out.println("Size of Obs: " + movables.size() + "\n");
        return ts.hasCollision(rc, movables);

    }


    /**
     *
     * @param state state for comparison
     * @return true if the given state equals to this state, false otherwise
     */
    @Override
    public boolean equals (Object state) {
        if(!(state instanceof State)) {
            return false;
        }

        State st = (State) state;
        if(st.getRobot().equals(this.getRobot())) {
            if(st.getBoxes().equals(this.getBoxes())) {
                if(st.getMovingObst().equals(this.getMovingObst())) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Get the first point of the robot
     * @param r the robot
     * @param ps the problem spec that this robot config is from
     * @return A Point2D representing the first point.
     */
    public Point2D getPoint2(RobotConfig r, ProblemSpec ps) {
        double x = r.getPos().getX() + Math.cos(r.getOrientation()) * ps.getRobotWidth() * 0.5;
        double y = r.getPos().getY() + Math.sin(r.getOrientation()) * ps.getRobotWidth() * 0.5;
        return new Point2D.Double(x,y);
    }
    /**
     * Get the second point of the robot
     * @param r the robot
     * @param ps the problem spec that this robot config is from
     * @return A Point2D representing the second point.
     */
    public Point2D getPoint1(RobotConfig r, ProblemSpec ps) {
        double x = r.getPos().getX() - Math.cos(r.getOrientation()) * ps.getRobotWidth() * 0.5;
        double y = r.getPos().getY() - Math.sin(r.getOrientation()) * ps.getRobotWidth() * 0.5;
        return new Point2D.Double(x,y);
    }

    /**
     * TODO: TO BE IMPLEMENTED
     * Get the cost from current state to given state s
     * @param s given goal state
     * @return cost cost from current state to given state s
     */
    public double costToState(State s) {
        return 0;
    }

    /**
     * TODO: TO BE IMPLEMENTED
     * Get all state that this state can reach
     * @return all neighbours of this state
     */
//    public List<State> getNeighbour() {
//        List<State> neighbours = new ArrayList<State>();
//
//        return neighbours;
//    }

    /**
     * TODO: TO BE IMPLEMENTED
     * An estimation of cost from current state to s.
     * The heuristic could be the Euclidean distance between the position at current state
     * and the position at goal sate
     * @param s goal state
     * @return estimation.
     */
    public double heuristic(State s) {

        return 0.0;
    }


}
