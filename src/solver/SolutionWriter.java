package solver;

import problem.Box;
import problem.MovingBox;
import problem.MovingObstacle;
import problem.RobotConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * A class that writes path to an output txt file
 */
public class SolutionWriter {
//    private String outputFileName;
//
//    SolutionWriter(String outputFileName) {
//        this.outputFileName = outputFileName;
//    }

    public SolutionWriter() {
    }
    /**
     * Write the state path to given file name
     * @param statePath
     * @param fileName the file name that we write to
     * @throws IOException
     */
    public void writePath(List<State> statePath, String fileName) throws IOException {
        BufferedWriter bw = null;
        File outputFile = new File(fileName);
        if(!outputFile.exists()) {
            outputFile.createNewFile();
        }
        bw = new BufferedWriter(new FileWriter(outputFile, false));
        //write steps in first line
        bw.write(statePath.size()+"");
        bw.newLine();
        //write every state steps
        for (State s : statePath) {
            bw.write(s.toString());
            bw.newLine();
        }

        System.out.println("File Written Successfully");
        bw.close();
    }

}
