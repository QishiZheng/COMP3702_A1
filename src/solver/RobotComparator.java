package solver;

import problem.RobotConfig;

import java.util.Comparator;

/**
 * Comparator for RobotConfig
 */
public class RobotComparator implements Comparator<RobotConfig> {
    private RobotConfig currentRobot;

    //set the current robot
    public void setRobot(RobotConfig rConf) {
        this.currentRobot = rConf;
    }


    /**
     * Compare the distance of the robotConfig a and b to the current robotConfig
     * @param a robotConfig a
     * @param b robotConfig b
     * @return -1 if the distance from a to current is shorter than b to current.
     *          1 if the distance from a to current is longer than b to current
     *          0 if equal
     */
    @Override
    public int compare(RobotConfig a, RobotConfig b) {
        Double ca = currentRobot.getPos().distance(a.getPos());
        Double cb = currentRobot.getPos().distance(b.getPos());
        if(ca < cb) {
            return -1;
        } else if (ca > cb) {
            return 1;
        } else {
            return 0;
        }
    }
}
