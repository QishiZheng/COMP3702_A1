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
        Double ca = distanceBetween(this.currentRobot, a);
        Double cb = distanceBetween(this.currentRobot, b);
        if(ca < cb) {
            return -1;
        } else if (ca > cb) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Calculate the distance between two robotConfigs,
     * A RobotConfig is treated as a 3d point here
     * @param r1 RobotConfig 1
     * @param r2 RobotConfig 2
     * @return The distance between r1 and r2
     */
    private double distanceBetween(RobotConfig r1, RobotConfig r2) {
        return Math.sqrt(Math.pow(r1.getPos().getX() - r2.getPos().getX(), 2)
                         + Math.pow(r1.getPos().getY() - r2.getPos().getY(), 2)
                         + Math.pow(r1.getOrientation() - r2.getOrientation(), 2));
    }
}
