package task2;

import task1.World;
import java.util.Scanner;


class Simulator {

    private Scanner sc;
    private int trials, trainingRounds, steps;
    private boolean stepByStep;

    private Simulator(){
        sc = new Scanner(System.in);
        trials = 1;
        trainingRounds = 100;
        steps = 50;
        stepByStep = false;
    }

    private void runSimulation(){
        int sum = 0;
        for (int i = 1; i <= trials; i++) {
            int trialScore = runTrial();
            System.out.println("Trial\t" + i + "\tscore: " + trialScore);
            sum += trialScore;
        }
        System.out.println("---------------------\nTotal avg. score: " + sum/trials);
    }

    private int runTrial(){
        SupervisedNeuralAgent agent = new SupervisedNeuralAgent();
        for (int i = 1; i <= trainingRounds; i++) {
            int roundScore = runTrainingRound(agent);
            System.out.println("\tRound\t" + i + "\tscore: " + roundScore);
        }
        return agent.getScore();
    }

    private int runTrainingRound(SupervisedNeuralAgent agent){
        World world = new World();
        world.placeAgentRandom();
        agent.registerNewWorld(world);
        if (stepByStep) {
            System.out.println("Initial world:");
            printGrid(world.getGrid());
            System.out.println();
        }
        int step = 1;
        while(!world.simulationEnd && step <= steps) {
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
        Simulator simulator = new Simulator();
        simulator.runSimulation();
    }
}
