package tester;

import problem.*;
import solver.Astar;
import solver.State;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AstarTest {
    public static void main(String[] args) {
        // Configure here //
        ProblemSpec ps = new ProblemSpec();
        try {
            ps.loadProblem("input2.txt");
        } catch (IOException ioe) {
            System.out.println("FAILED: Invalid problem file");
            System.out.println(ioe.getMessage());
        }
        // End Configuration //

        Astar astar = new Astar(ps);
        List<LinkedList<State>> listOfStates = astar.getStateList();
        int counter = 0;
        for (LinkedList<State> stateList : listOfStates) {
            System.out.println(counter);
            for (State state : stateList) {
                System.out.println(state.toString());

            }
            counter++;
        }
    }
}
