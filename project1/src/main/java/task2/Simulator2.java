package task2;

import javafx.application.Application;
import task1.World;
import common.Plotter;
import common.GUI;
import java.io.*;

class Simulator2 {

    private int trainingRounds, trials, steps;
    private Plotter plotter;
    private String taskName;
    private BufferedWriter writer;
    private SupervisedAgent agent;

    private Simulator2() throws FileNotFoundException {
        taskName = "Task 2 – Supervised agent";
        trainingRounds = 50;
        trials = 100;
        steps = 50;
        plotter = new Plotter(taskName, "Training round", "Average score", trainingRounds);
        File gridStorageFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\common\\gridStorageFile.txt");
        writer = new BufferedWriter(new PrintWriter(gridStorageFile));
    }

    private void runSimulation() throws IOException {
        agent = new SupervisedAgent();
        double totalScore = 0;
        for (int i = 1; i <= trainingRounds; i++) {
            writer.write("Round");
            writer.newLine();
            double roundAvgScore = runTrainingRound(agent);
            System.out.println(String.format("%s%5d%s%6.1f", "Training round", i, "  avg score:", roundAvgScore));
            totalScore += roundAvgScore;
            writer.write("End of round");
            writer.newLine();
            plotter.addData(i, roundAvgScore);
        }
        System.out.println(String.format("%s%.1f", "--------------------------\nTotal avg score: ", totalScore/trainingRounds));
        System.out.println("\n" + taskName);
        System.out.println("\nSETTINGS");
        System.out.println("Training rounds: " + trainingRounds);
        System.out.println("Trials: " + trials);
        writer.close();
        plotter.plot();
    }

    private double runTrainingRound(SupervisedAgent agent) throws IOException {
        double roundScore = 0;
        for (int i = 1; i <= trials; i++) {
            writer.write("Trial");
            writer.newLine();
            int trialScore = runTrial(agent);
            roundScore += trialScore;
        }
        return roundScore/trials;
    }

    private int runTrial(SupervisedAgent agent) throws IOException {
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
        writer.write(":" + agent.getScore());
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
        Simulator2 simulator = new Simulator2();
        simulator.runSimulation();
        GUI gui = new GUI();
        Application.launch(gui.getClass());
    }
}
