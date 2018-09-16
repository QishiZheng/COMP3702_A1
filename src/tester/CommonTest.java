package tester;

import problem.ProblemSpec;
import solver.State;

import java.io.IOException;

public class CommonTest {
    public static void main(String[] args) {
        ProblemSpec ps = new ProblemSpec();
        try {
            ps.loadProblem("input1.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create a initial state from the problrmSpec
        State s = new State(ps);
        System.out.println(s);
    }



}
