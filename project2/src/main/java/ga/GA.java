package ga;

import representation.*;
import tools.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class GA {

    private String mapName;
    private int popSize, maxIterations, eliteAmount;

    public GA() {
        // SETTINGS
        mapName = "p03";
        popSize = 200;     // 1000
        maxIterations = 100;  // 1000
        eliteAmount = popSize/100;
    }

    private void runAlgorithm() throws IOException, InterruptedException {
        Map map = DataReader.readMapData(mapName);

        ArrayList<Solution> population = new ArrayList<>();
        // Initial population
        for (int i = 0; i < popSize; i++) {
            population.add(new Solution(map));
        }
        Plotter plotter = new Plotter(map);


        Solution bestSolution = population.get(0);
        // Evolution
        for (int i = 0; i < maxIterations; i++) {
            bestSolution = findBestSolution(population);
            System.out.print("Duration: " + (int)bestSolution.getTotalDuration() + "\t");
            System.out.println("Cost: " + (int)bestSolution.getCost());
            population = clonePopulation(bestSolution);
            plotter.plotSolution(bestSolution);
        }
        TimeUnit.SECONDS.sleep(1);
        plotter.plotSolution(bestSolution);
        System.out.println(bestSolution.getTotalDuration());
    }

    private Solution findBestSolution(ArrayList<Solution> population) {
        Solution bestSolution = population.get(0);
        double bestSolutionDist = bestSolution.getCost();
        for (int i = 1; i < population.size(); i++) {
            if (population.get(i).getCost() < bestSolutionDist) {
                bestSolution = population.get(i);
                bestSolutionDist = population.get(i).getCost();
            }
        }
        return bestSolution;
    }

    private ArrayList<Solution> clonePopulation(Solution solution) {
        ArrayList<Solution> clonedPopulation = new ArrayList<>();
        clonedPopulation.add(new Solution(solution, false));
        for (int i = 1; i < popSize; i++) {
            clonedPopulation.add(new Solution(solution, true));
        }
        return clonedPopulation;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        GA ga = new GA();
        ga.runAlgorithm();
    }
}


