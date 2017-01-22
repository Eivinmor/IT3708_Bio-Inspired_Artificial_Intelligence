package task2;

import java.util.Scanner;


class Simulator {

    private Scanner sc;
    private int trials, rounds, steps;
    private boolean stepByStep;

    private Simulator(){
        sc = new Scanner(System.in);
        trials = 1;
        rounds = 100;
        steps = 100;
        stepByStep = false;
    }

    private void runSimulation(){
        int sum = 0;
        for (int i = 1; i <= trials; i++) {
            int score = runTrial();
            System.out.println("Trial " + i + " score: " + score);
            sum += score;
        }
        System.out.println("---------------------\nTotal avg. score: " + sum/trials);
    }

    private int runTrial(){
        World world = new World();
        SupervisedNeuralAgent agent = new SupervisedNeuralAgent(world);
        world.placeAgent(agent.getY(), agent.getX());
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
