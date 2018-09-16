package solver;

import problem.*;

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


    /**
     * Constructs a state from a ProblemSpec
     * @param ps Given ProblemSpec for constructing state
     */
    public State(ProblemSpec ps) {
        this.robotState = ps.getInitialRobotConfig();
        this.boxState = ps.getMovingBoxes();
        this.movingObstState = ps.getMovingObstacles();
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
