package ga;

import utility.Tools;
import java.util.ArrayList;


public class NSGA2 {

    public void runAlgorithm() {
        ArrayList<Chromosome> population = createInitialPopulation();

        int generation = 1;
        while (true) {
            if (generation % Settings.generationsPerPause == 0) {
                Tools.printPause(generation, population.get(0));
            }
            generation++;
        }

    }

    private ArrayList<Chromosome> createInitialPopulation() {
        ArrayList<Chromosome> initialPopulation = new ArrayList<>(Settings.populationSize * 2);
        // TODO Do shit
        return initialPopulation;
    }

    private ArrayList<Chromosome> createOffspringPopulation(ArrayList<Chromosome> population) {
         ArrayList<Chromosome> offsprintPopulation = new ArrayList<>(Settings.populationSize);
         // TODO Do shit
         return offsprintPopulation;
    }

    private void calculateFronts() {
        // TODO Do heavy shit
    }

    private void crowdingTournament() {
        // TODO 1. Rank
        // TODO 2. Crowding distance
    }

}
