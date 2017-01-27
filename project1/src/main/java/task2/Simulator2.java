package task2;

import task1.World;
import common.Plotter;

class Simulator2 {

    private int trainingRounds, trials, steps, currentTrainingRound, currentTrial;
    private Plotter plotter;
    private String taskName;
    private char[][][][][] gridStorage;

    private Simulator2(){
        taskName = "Task 2 – Supervised neural agent";
        trainingRounds = 100;
        trials = 100;
        steps = 50;
        plotter = new Plotter(taskName, "Training round", "Average score", trainingRounds);
        gridStorage = new char[trainingRounds][trials][steps+1][10][10];
    }

    private void runSimulation(){
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
        double roundScore = 0;
        for (int i = 1; i <= trials; i++) {
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
        int step = 1;
        gridStorage[currentTrainingRound-1][currentTrial-1][step-1] = world.getGrid();
        while(!world.simulationEnd && step <= steps) {
            agent.step();
            step++;
            gridStorage[currentTrainingRound-1][currentTrial-1][step-1] = world.getGrid();
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
