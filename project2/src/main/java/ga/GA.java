package ga;

import representation.*;
import tools.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class GA {

    private String mapName;
    private int popSize, maxIterations, eliteAmount, tournamentSize, stopWithinPercent;
    private Random random;

    public GA() {
        // SETTINGS
        mapName = "p01";
        popSize = 200;     // 1000
        maxIterations = 10000;  // 1000
        eliteAmount = popSize/10 + 1;
        tournamentSize = 2;
        stopWithinPercent = 1;
    }

    private void runAlgorithm() throws IOException, InterruptedException {
        Map map = DataReader.readMapData(mapName);
        random = new Random();
        ArrayList<Solution> population = new ArrayList<>();

        // INITIAL POPULATION
        for (int i = 0; i < popSize; i++) {
            population.add(new Solution(map));
        }
        Plotter plotter = new Plotter(map);
        Collections.sort(population);
        Solution bestSolution = population.get(0);

        // EVOLUTION
        for (int i = 0; i < maxIterations; i++) {

            ArrayList<Solution> elite = new ArrayList<>(population.subList(0, eliteAmount));
            ArrayList<Solution> newPopulation = new ArrayList<>(population.subList(0, 1));
            for (int j = 0; j < elite.size(); j++) {
                newPopulation.add(new Solution(elite.get(j), true));
            }

            while (newPopulation.size() < popSize) {
                newPopulation.add(new Solution(tournamentSelection(tournamentSize, population), true));
            }
            population = newPopulation;
            Collections.sort(population);
            bestSolution = population.get(0);

            // PRINT AND WRITE FILE
            double percentOverOptimal = (bestSolution.getTotalDuration()/map.optimalDuration)*100-100;
            System.out.print("Iteration: " + (i+1) + "\t\t");
            System.out.print("Duration: " + (int)bestSolution.getTotalDuration() + "\t");
            System.out.print("Cost: " + (int)bestSolution.getCost() + "\t\t");
            System.out.println(String.format(Locale.US, "%.1f", percentOverOptimal) + "% over optimal");
            plotter.plotSolution(bestSolution);

            if (percentOverOptimal < stopWithinPercent) {
                System.out.println("\nWithin " + stopWithinPercent + "% of optimal. Stopping.");
                break;
            }
        }
        TimeUnit.MILLISECONDS.sleep(200);
        plotter.plotSolution(bestSolution);
        System.out.println("-------------------------------");
        System.out.println("Optimal: " + map.optimalDuration);
        System.out.println("Achieved: " + String.format(Locale.US, "%.2f", bestSolution.getTotalDuration()));
        DataReader.writeSolutionToFile(bestSolution);
    }

    private Solution findBestSolution(ArrayList<Solution> population) {
        Solution bestSolution = population.get(0);
        double bestSolutionCost = bestSolution.getCost();
        for (int i = 1; i < population.size(); i++) {
            if (population.get(i).getCost() < bestSolutionCost) {
                bestSolution = population.get(i);
                bestSolutionCost = population.get(i).getCost();
            }
        }
        return bestSolution;
    }

    private ArrayList<Solution> clonePopulation(Solution solution) {
        ArrayList<Solution> clonedPopulation = new ArrayList<>();
        clonedPopulation.add(new Solution(solution, false));
        for (int i = 1; i < popSize; i++)
            clonedPopulation.add(new Solution(solution, true));
        return clonedPopulation;
    }

    private Solution tournamentSelection(int tournamentSize, ArrayList<Solution> population) {
        ArrayList<Solution> tournamentSelection = new ArrayList<>();
        for (int j = 0; j < tournamentSize; j++)
            tournamentSelection.add(population.get(random.nextInt(population.size())));
        return findBestSolution(tournamentSelection);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        GA ga = new GA();
        ga.runAlgorithm();
    }
}


