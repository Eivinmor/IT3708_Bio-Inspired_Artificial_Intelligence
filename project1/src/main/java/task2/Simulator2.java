package task2;

import common.Plotter;
import task1.World;
import java.util.Scanner;


public class Simulator2 {

    private Scanner sc;
    private int trials, trainingRounds, steps;
    private boolean stepByStep;
    private Plotter plotter;
    private char[][][][][] gridStorage;


    public Simulator2(){
        sc = new Scanner(System.in);
        trainingRounds = 100;
        trials = 100;
        steps = 50;
        stepByStep = false;
        plotter = new Plotter("Task 2 – Supervised neural agent", "Training round", "Average score", trainingRounds);
        gridStorage = new char[trainingRounds][trials][steps][10][10];
    }

    public char[][][][][] runSimulation(){
        SupervisedNeuralAgent agent = new SupervisedNeuralAgent();
        double totalScore = 0;
        for (int i = 1; i <= trainingRounds; i++) {
            double roundAvgScore = runTrainingRound(agent, i);
            System.out.println(String.format("%s%5d%s%6.1f", "Training round", i, "  avg score:", roundAvgScore));
            totalScore += roundAvgScore;
            plotter.addData(i, roundAvgScore);
        }
        System.out.println(String.format("%s%.1f", "--------------------------\nTotal avg score: ", totalScore/trainingRounds));
        System.out.println("\nTask 2 – Supervised neural agent");
        System.out.println("\nSETTINGS");
        System.out.println("Training rounds: " + trainingRounds);
        System.out.println("Trials: " + trials);
        plotter.plot();
        return gridStorage;
    }

    private double runTrainingRound(SupervisedNeuralAgent agent, int trainingRound){
        double roundScore = 0;
        for (int i = 1; i <= trials; i++) {
            int trialScore = runTrial(agent, trainingRound, i);
            roundScore += trialScore;
        }
        return roundScore/trials;
    }

    private int runTrial(SupervisedNeuralAgent agent, int trainingRound, int trial){
        World world = new World();
        agent.registerNewWorld(world);
        world.placeAgentRandom();
        if (stepByStep) {
            System.out.println("Initial world:");
            printGrid(world.getGrid());
            System.out.println();
        }
        int step = 1;
        while(!world.simulationEnd && step <= steps) {
            gridStorage[trainingRound-1][trial-1][step-1] = world.getGrid();
            if (stepByStep) {
                sc.nextLine();
                agent.step();
                printGrid(world.getGrid());
                System.out.println("Step " + step + " score: " + agent.getScore() + "\n");
            }
            else agent.step();
            step++;
        }
        return agent.getScore();
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
