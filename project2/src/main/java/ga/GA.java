package ga;

import representation.*;
import tools.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class GA {

    private String mapName;
    private int popSize, maxIterations, eliteAmount, tournamentSize;
    private Random random;

    public GA() {
        // SETTINGS
        mapName = "p02";
        popSize = 100;     // 1000
        maxIterations = 1000;  // 1000
        eliteAmount = popSize/10 + 1;
        tournamentSize = 2;
    }

    private void runAlgorithm() throws IOException, InterruptedException {
        Map map = DataReader.readMapData(mapName);
        random = new Random();
        ArrayList<Solution> population = new ArrayList<>();
        // Initial population
        for (int i = 0; i < popSize; i++) {
            population.add(new Solution(map));
        }
        Plotter plotter = new Plotter(map);
        Collections.sort(population);
        Solution bestSolution = population.get(0);


        // Evolution
        for (int i = 0; i < maxIterations; i++) {

            ArrayList<Solution> elite = new ArrayList<>(population.subList(0, eliteAmount));
            ArrayList<Solution> newPopulation = new ArrayList<>(population.subList(0, 1));
            for (int j = 0; j < elite.size(); j++) {
                newPopulation.add(new Solution(elite.get(j), true));
            }

            while (newPopulation.size() < popSize) {
                ArrayList<Solution> tournamentSelection = new ArrayList<>();
                for (int j = 0; j < tournamentSize; j++) {
                    tournamentSelection.add(population.get(random.nextInt(population.size())));
                }

                newPopulation.add(new Solution(findBestSolution(tournamentSelection), true));
//                System.out.println(population.get(population.size()-1).getCost());
            }
            population = newPopulation;
            Collections.sort(population);
            bestSolution = population.get(0);

            System.out.print("Duration: " + (int)bestSolution.getTotalDuration() + "\t");
            System.out.println("Cost: " + (int)bestSolution.getCost());
            plotter.plotSolution(bestSolution);
        }
        TimeUnit.MILLISECONDS.sleep(200);
        plotter.plotSolution(bestSolution);
        System.out.println("-------------------------------\n" + bestSolution.getTotalDuration());
        DataReader.writeSolutionToFile(bestSolution);
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


