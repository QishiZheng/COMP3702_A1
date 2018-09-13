package solver;

import problem.ProblemSpec;
import problem.RobotConfig;

/**
 * PRM algorithm for sampling configuration for the robot
 */
public class PRM {
    private ProblemSpec ps;

    //constructs PRM and load problem spec
    public PRM(ProblemSpec problem) {
        this.ps = problem;
    }

    /**
     * Build stategraph for robot config from init state to goal state
     * @param init
     * @param goal
     * @return a graph that contains path from init state to goal state
     */
    public StateGraph<RobotConfig> buildGraph(RobotConfig init , RobotConfig goal) {
        Vertex<RobotConfig> initVertex = new Vertex<>(init);
        Vertex<RobotConfig> goalVertex = new Vertex<>(goal);
        StateGraph<RobotConfig> sg = new StateGraph<>(initVertex, goalVertex);
        //TODO: NEED TO BE IMPLEMENTED

        return sg;
    }

}
