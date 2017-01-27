package task2;

import common.Plotter;
import task1.World;

import java.util.ArrayList;
import java.util.Scanner;


public class Simulator2 {

    private Scanner sc;
    private int trials, trainingRounds, steps;
    private boolean stepByStep;
    private Plotter plotter;


    public Simulator2(){
        sc = new Scanner(System.in);
        trainingRounds = 100;
        trials = 100;
        steps = 50;
        stepByStep = false;
        plotter = new Plotter("Task 2 – Supervised neural agent", "Training round", "Average score", trainingRounds);
    }

    public ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Character>>>>> runSimulation(){
        SupervisedNeuralAgent agent = new SupervisedNeuralAgent();
        ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Character>>>>> gridStorage = new ArrayList<>();

        for (int i = 1; i <= trainingRounds; i++) {
            gridStorage.add(runTrainingRound(agent, i));
        }
        System.out.println("--------------------------");
        System.out.println("\nTask 2 – Supervised neural agent");
        System.out.println("\nSETTINGS");
        System.out.println("Training rounds: " + trainingRounds);
        System.out.println("Trials: " + trials);
        plotter.plot();
        return gridStorage;
    }

    private ArrayList<ArrayList<ArrayList<ArrayList<Character>>>> runTrainingRound(SupervisedNeuralAgent agent, int number){
        ArrayList<ArrayList<ArrayList<ArrayList<Character>>>> trainingRoundGridStorage = new ArrayList<>();
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
        ArrayList<ArrayList<ArrayList<Character>>> trialGridStorage = new ArrayList<>();
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
