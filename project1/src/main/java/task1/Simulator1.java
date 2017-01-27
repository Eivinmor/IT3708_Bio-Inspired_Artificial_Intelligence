package task1;

import javafx.application.Application;
import common.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Simulator1 {

    private Scanner sc;
    private int trials, steps;
    private boolean stepByStep;
    private Plotter plotter;
    private char[][][][][] gridStorage;

    public Simulator1(){
        sc = new Scanner(System.in);
        trials = 1000;
        steps = 50;
        stepByStep = false;
        plotter = new Plotter("Task 1 – Baseline agent", "Trial", "Score", trials);
        gridStorage = new char[1][trials][steps][10][10];
    }

    public char[][][][][] runSimulation(){
        BaselineAgent agent = new BaselineAgent();
        int totalScore = 0;
        for (int i = 1; i <= trials; i++) {
            int trialScore = runTrial(agent, i);
            System.out.println(String.format("%s%5d%s%4d", "Trial", i, "  score:", trialScore));
            totalScore += trialScore;
            plotter.addData(i, trialScore);
        }
        System.out.println(String.format("%s%.1f", "--------------------------\nTotal avg score: ", (double)totalScore/trials));
        System.out.println("\nTask 1 – Baseline agent");
        System.out.println("\nSETTINGS");
        System.out.println("Trials: " + trials);
        plotter.plot();
        return gridStorage;
    }

    private int runTrial(BaselineAgent agent, int trialNumber){
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
            if (stepByStep) {
                sc.nextLine();
                agent.step();
                printGrid(world.getGrid());
                System.out.println("Step " + step + " score: " + agent.getScore() + "\n");
            }
            else agent.step();
            gridStorage[0][trialNumber-1][step-1] = world.getGrid();
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
        Simulator1 simulator1 = new Simulator1();
        simulator1.runSimulation();
    }


}
