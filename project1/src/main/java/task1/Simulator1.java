package task1;

import common.GUI;
import javafx.application.Application;

import java.io.*;

class Simulator1 {

    private int trials, steps;
    private common.Plotter plotter;
    private String taskName;
    private BufferedWriter writer;

    private Simulator1() throws FileNotFoundException {
        taskName = "Task 1 – Baseline agent";
        trials = 1000;
        steps = 50;
        plotter = new common.Plotter(taskName, "Trial", "Score", trials);
        File gridStorageFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\common\\gridStorageFile.txt");
        writer = new BufferedWriter(new PrintWriter(gridStorageFile));
    }

    private void runSimulation() throws IOException {
        writer.write("Round");
        writer.newLine();
        BaselineAgent agent = new BaselineAgent();
        int totalScore = 0;
        for (int i = 1; i <= trials; i++) {
            writer.write("Trial");
            writer.newLine();
            int trialScore = runTrial(agent);
            System.out.println(String.format("%s%5d%s%4d", "Trial", i, "  score:", trialScore));
            totalScore += trialScore;
            plotter.addData(i, trialScore);
        }
        System.out.println(String.format("%s%.1f", "--------------------------\nTotal avg score: ", (double)totalScore/trials));
        System.out.println("\nTask 1 – Baseline agent");
        System.out.println("\nSETTINGS");
        System.out.println("Trials: " + trials);
        writer.write("End of round");
        writer.close();
        plotter.plot();
    }

    private int runTrial(BaselineAgent agent) throws IOException {
        World world = new World();
        agent.registerNewWorld(world);
        world.placeAgentRandom();
        int step = 1;
        writeGridToFile(world.getGrid());
        while(!world.simulationEnd && step <= steps) {
            agent.step();
            step++;
            writeGridToFile(world.getGrid());
        }
        return agent.getScore();
    }

    private void writeGridToFile(char[][] grid) throws IOException {
        for (int i = 0; i < grid.length; i++) {
            writer.write(grid[i]);
            writer.write(",");
        }
        writer.newLine();
    }

    private void printGrid(char[][] grid){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        Simulator1 simulator1 = new Simulator1();
        simulator1.runSimulation();
        GUI gui = new GUI();
        Application.launch(gui.getClass());
    }
}
