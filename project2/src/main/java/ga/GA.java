package ga;

import representation.*;
import tools.*;
import java.io.IOException;


public class GA {

    private String mapName;
    private Plotter plotter;
    private int popSize;

    public GA() {
        // SETTINGS
        mapName = "p01";
        popSize = 1000;


    }

    private Solution runAlgorithm() throws IOException {
        Map map = DataReader.readMapData(mapName);
        plotter = new Plotter(map.name);
        plotter.addScatterSeries("Depots", map.depots);
        plotter.addScatterSeries("Customers", map.customers);
        plotter.init();

        Solution solution = new Solution(map);
        plotter.plotSolution(solution);

        return solution;
    }

//    private Solution findBestSolution(Solution[] population) {
//        Solution bestSolution = population[0];
//        double bestSolutionDist = bestSolution.getTotalDistance();
//        for (int i = 1; i < population.length; i++) {
//            if (population[i].getTotalDistance() < bestSolutionDist) {
//                bestSolution = population[i];
//                bestSolutionDist = population[i].getTotalDistance();
//            }
//        }
//        return bestSolution;
//    }
//
//    private Solution[] clonePopulation(Solution solution) {
//        Solution[] clonedPopulation = new Solution[popSize];
//        clonedPopulation[0] = new Solution(solution);
//        for (int i = 1; i < popSize; i++) {
//            clonedPopulation[i] = new Solution(solution);
//            clonedPopulation[i].mutate();
//        }
//        return clonedPopulation;
//    }

    public static void main(String[] args) throws IOException {
        GA ga = new GA();
        ga.runAlgorithm();
    }
}


