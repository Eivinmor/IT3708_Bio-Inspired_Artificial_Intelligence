package task2;

import common.Plotter;
import task1.World;

import java.util.ArrayList;


public class Simulator2 {

    private int trials, trainingRounds, steps;
    private Plotter plotter;
    private String taskName;


    public Simulator2(){
        trainingRounds = 1000;
        trials = 100;
        steps = 50;
        taskName = "Task 2 – Supervised neural agent";
        plotter = new Plotter(taskName, "Training round", "Average score", trainingRounds);
    }

    public ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Character>>>>> runSimulation(){
        SupervisedNeuralAgent agent = new SupervisedNeuralAgent();
        ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Character>>>>> gridStorage = new ArrayList<>(trainingRounds);

        for (int i = 1; i <= trainingRounds; i++) {
            gridStorage.add(runTrainingRound(agent, i));
        }
        System.out.println("--------------------------");
        System.out.println("\n" + taskName);
        System.out.println("\nSETTINGS");
        System.out.println("Training rounds: " + trainingRounds);
        System.out.println("Trials: " + trials);
        plotter.plot();
        return gridStorage;
    }

    private ArrayList<ArrayList<ArrayList<ArrayList<Character>>>> runTrainingRound(SupervisedNeuralAgent agent, int number){
        ArrayList<ArrayList<ArrayList<ArrayList<Character>>>> trainingRoundGridStorage = new ArrayList<>(trials);
        double roundScore = 0;
        for (int i = 1; i <= trials; i++) {
            trainingRoundGridStorage.add(runTrial(agent));
            roundScore += agent.getScore();
        }
        double roundAvgScore = roundScore/trials;
        System.out.println(String.format("%s%5d%s%6.1f", "Training round", number, "  avg score:", roundAvgScore));
        plotter.addData(number, roundAvgScore);
        return trainingRoundGridStorage;
    }

    private ArrayList<ArrayList<ArrayList<Character>>> runTrial(SupervisedNeuralAgent agent){
        ArrayList<ArrayList<ArrayList<Character>>> trialGridStorage = new ArrayList<>(steps);
        World world = new World();
        agent.registerNewWorld(world);
        world.placeAgentRandom();

        int step = 1;
        trialGridStorage.add(world.getGridArrayList());
        while(!world.simulationEnd && step <= steps) {
            agent.step();
            trialGridStorage.add(world.getGridArrayList());
            step++;
        }
        return trialGridStorage;
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
