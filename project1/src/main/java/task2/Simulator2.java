package task2;

import task1.World;
import common.Plotter;

import java.util.ArrayList;

public class Simulator2 {

    private int trainingRounds, trials, steps, currentTrainingRound, currentTrial;
    private Plotter plotter;
    private String taskName;
    private char[][][][] initialGrids;
    private ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>> positionStorage;

    public Simulator2(){
        taskName = "Task 2 – Supervised neural agent";
        trainingRounds = 1000;
        trials = 100;
        steps = 50;
        plotter = new Plotter(taskName, "Training round", "Average score", trainingRounds);
        initialGrids = new char[trainingRounds][trials][10][10];
        positionStorage = new ArrayList<>();
    }

    public void runSimulation(){
        SupervisedNeuralAgent agent = new SupervisedNeuralAgent();
        double totalScore = 0;
        for (int i = 1; i <= trainingRounds; i++) {
            currentTrainingRound = i;
            double roundAvgScore = runTrainingRound(agent);
            System.out.println(String.format("%s%5d%s%6.1f", "Training round", i, "  avg score:", roundAvgScore));
            totalScore += roundAvgScore;
            plotter.addData(i, roundAvgScore);
        }
        System.out.println(String.format("%s%.1f", "--------------------------\nTotal avg score: ", totalScore/trainingRounds));
        System.out.println("\n" + taskName);
        System.out.println("\nSETTINGS");
        System.out.println("Training rounds: " + trainingRounds);
        System.out.println("Trials: " + trials);
        plotter.plot();
    }

    private double runTrainingRound(SupervisedNeuralAgent agent){
        positionStorage.add(new ArrayList<>());
        double roundScore = 0;
        for (int i = 1; i <= trials; i++) {
            positionStorage.get(positionStorage.size()-1).add(new ArrayList<>());
            currentTrial = i;
            int trialScore = runTrial(agent);
            roundScore += trialScore;
        }
        return roundScore/trials;
    }

    private int runTrial(SupervisedNeuralAgent agent){
        World world = new World();
        agent.registerNewWorld(world);
        world.placeAgentRandom();
        initialGrids[currentTrainingRound-1][currentTrial-1] = world.getGrid();
        positionStorage.get(currentTrainingRound-1).get(currentTrial-1).add(world.getAgentPosition());
        int step = 1;
        while(!world.simulationEnd && step <= steps) {
            agent.step();
            positionStorage.get(currentTrainingRound-1).get(currentTrial-1).add(world.getAgentPosition());
            step++;
        }
        return agent.getScore();
    }

    public char[][][][] getInitialGrids(){
        return initialGrids;
    }

    public ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>> getPositionStorage(){
        return positionStorage;
    }

    private void printGrid(char[][] grid){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Simulator2 simulator2 = new Simulator2();
        simulator2.runSimulation();
    }
}
