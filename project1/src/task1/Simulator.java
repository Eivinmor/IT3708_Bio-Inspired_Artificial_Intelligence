package task1;

import java.util.Scanner;


class Simulator {

    private Scanner sc;
    private int trials;
    private boolean stepByStep;

    private Simulator(){
        sc = new Scanner(System.in);
        trials = 1;
        stepByStep = true;
    }

    private void simulate(){
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
        Agent agent = new Agent(world);
        if (stepByStep) {
            System.out.println("Initial world:");
            printGrid(world.getGrid());
            System.out.println();
        }
        int step = 0;
        while(!world.simulationEnd && step < 50) {
            if (stepByStep) {
                sc.nextLine();
                agent.step();
                printGrid(world.getGrid());
                System.out.println("Step " + step + " score: " + agent.getScore());
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
        simulator.simulate();
    }
}
