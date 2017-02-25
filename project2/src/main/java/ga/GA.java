package ga;

import representation.*;
import tools.*;
import java.io.IOException;
import java.util.ArrayList;


public class GA {

    private String mapName;
    private int popSize;

    public GA() {
        // SETTINGS
        mapName = "p02";
        popSize = 1;

    }

    private void runAlgorithm() throws IOException {
        Map map = DataReader.readMapData(mapName);

        ArrayList<Solution> population = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            population.add(new Solution(map));
        }
        Plotter plotter = new Plotter(map);
        Solution bestSolution = findBestSolution(population);
        plotter.plotSolution(bestSolution);
        System.out.println(bestSolution.getTotalDuration());

        Solution clone = new Solution(bestSolution);
        Plotter plotter2 = new Plotter(map);
        plotter2.plotSolution(clone);
        System.out.println(clone.getTotalDuration());
    }

    private Solution findBestSolution(ArrayList<Solution> population) {
        Solution bestSolution = population.get(0);
        double bestSolutionDist = bestSolution.getTotalDuration();
        for (int i = 1; i < population.size(); i++) {
            if (population.get(i).getTotalDuration() < bestSolutionDist) {
                bestSolution = population.get(i);
                bestSolutionDist = population.get(i).getTotalDuration();
            }
        }
        return bestSolution;
    }

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


