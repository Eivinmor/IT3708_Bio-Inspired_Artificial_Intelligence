package ga;

import ga.nsga2.NSGA2Chromosome;
import representation.Grid;
import utility.Tools;
import java.util.ArrayList;


public class NSGA2 {

    ArrayList<ArrayList<NSGA2Chromosome>> rankedPopulation;
    ArrayList<NSGA2Chromosome> population;

    public void runAlgorithm() {
        population = createInitialPopulation();

        int generation = 1;
        while (true) {
            if (generation % Settings.generationsPerPause == 0) {
                Tools.printPause(generation, population.get(0));
            }
            population.addAll(createOffspringPopulation(population));
            rankPopulationByNonDomination();
            selectNewPopulationFromRankedPopulation();

            generation++;
        }

    }

    private ArrayList<NSGA2Chromosome> createInitialPopulation() {
        NSGA2Chromosome mstChromosome = new NSGA2Chromosome();
        ArrayList<NSGA2Chromosome> initialPopulation = new ArrayList<>(Settings.populationSize * 2);
        for (int i = 0; i < Settings.populationSize; i++) {
            NSGA2Chromosome chromosome = new NSGA2Chromosome(mstChromosome);
            chromosome.removeKRandomEdges(Tools.random.nextInt((Grid.numOfPixels / 2 ) + 1));
            initialPopulation.add(chromosome);
        }
        return initialPopulation;
    }

    // TODO Calculate score before this
    private ArrayList<NSGA2Chromosome> createOffspringPopulation(ArrayList<NSGA2Chromosome> population) {
        ArrayList<NSGA2Chromosome> offsprintPopulation = new ArrayList<>(Settings.populationSize);
        for (int i = 0; i < Settings.populationSize; i++) {
            NSGA2Chromosome p1 = binaryTournament(population);
            NSGA2Chromosome p2 = binaryTournament(population);
            offsprintPopulation.add(new NSGA2Chromosome(p1, p2));
        }
        return offsprintPopulation;
    }

    private NSGA2Chromosome binaryTournament(ArrayList<NSGA2Chromosome> population) {
        NSGA2Chromosome c1 = population.get(Tools.random.nextInt(Settings.populationSize));
        NSGA2Chromosome c2 = population.get(Tools.random.nextInt(Settings.populationSize));
        if (c1.compareTo(c2) < 0) return c1;
        else return c2;
    }

    private void rankPopulationByNonDomination() {
        // Generate numOfDominators and domnates set
        for (NSGA2Chromosome c1 : population) {
            for (NSGA2Chromosome c2 : population) {
                // TODO loop diagonal and check both ways
                if (c1.dominates(c2)) {
                    c1.dominates.add(c2);
                    c2.numOfDominators++;
                }
            }
        }
        // Add to ranks depending on domination relationships
        // TODO Stop generating ranks when total assigned > Settings.populationSize
        rankedPopulation.clear();
        int totalRanked = 0;
        while (totalRanked < Settings.populationSize) {
            ArrayList<NSGA2Chromosome> rank = new ArrayList<>();
            int i = 0;
            while (i < population.size()){
                if(population.get(i).numOfDominators == 0){
                    NSGA2Chromosome chromosome = population.remove(i);
                    for (NSGA2Chromosome dominated : chromosome.dominates) {
                        dominated.numOfDominators--;
                    }
                    rank.add(chromosome);
                }
                else i++;
            }
            rankedPopulation.add(rank);
            totalRanked += rank.size();
        }
    }


    private void selectNewPopulationFromRankedPopulation() {
        for (ArrayList<NSGA2Chromosome> rank : rankedPopulation) {
            if (rank.size() <= Settings.populationSize - population.size()) {
                assignCrowdingDistance(rank);
                population.addAll(rank);
            }
            else {
                assignCrowdingDistance(rank);
                while (population.size() < Settings.populationSize) population.add(binaryTournament(rank));
                return;
            }
        }
    }

    private void assignCrowdingDistance(ArrayList<NSGA2Chromosome> rank) {
        
    }


}
