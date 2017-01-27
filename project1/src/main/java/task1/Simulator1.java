package task1;

import common.Plotter;

import java.util.ArrayList;
import java.util.Scanner;

public class Simulator1 {

    private Scanner sc;
    private int trials, steps, score;
    private Plotter plotter;
    private ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Character>>>>> gridStorage;


    public Simulator1(){
        sc = new Scanner(System.in);
        trials = 1000;
        steps = 50;
        plotter = new Plotter("Task 1 – Baseline agent", "Trial", "Score", trials);
        gridStorage = new ArrayList<>();
    }

    public ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Character>>>>> runSimulation(){
        BaselineAgent agent = new BaselineAgent();
        gridStorage.add(new ArrayList<>());
        int totalScore = 0;
        for (int i = 1; i <= trials; i++) {
            gridStorage.get(0).add(runTrial(agent));
            System.out.println(String.format("%s%5d%s%4d", "Trial", i, "  score:", score));
            plotter.addData(i, score);
            totalScore += score;
        }
        System.out.println(String.format(
                "%s%.1f", "--------------------------\nTotal avg score: ", (double)totalScore/trials));
        System.out.println("\nTask 1 – Baseline agent");
        System.out.println("\nSETTINGS");
        System.out.println("Trials: " + trials);
        plotter.plot();
        return gridStorage;
    }

    private ArrayList<ArrayList<ArrayList<Character>>> runTrial(BaselineAgent agent){
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
        score = agent.getScore();
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
        Simulator1 simulator1 = new Simulator1();
        simulator1.runSimulation();
    }


}
