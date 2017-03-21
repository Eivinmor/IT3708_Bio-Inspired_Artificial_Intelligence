package ga.spea2;

import ga.Settings;

import java.util.ArrayList;

public class SPEA2 {

    private ArrayList<SPEA2Chromosome> population;
    private ArrayList<SPEA2Chromosome> archive;
    private int archiveSize = 50;

    public void runAlgorithm() {
        population = initialisePopulation();
        archive = new ArrayList<>(archiveSize);
        assignFitness(population);
        assignFitness(archive);
        archive = environmentalSelection();

    }

    private ArrayList<SPEA2Chromosome> initialisePopulation() {
        ArrayList<SPEA2Chromosome> newPopulation = new ArrayList<>(Settings.populationSize);
        SPEA2Chromosome mstChromosome = new SPEA2Chromosome();
        for (int i = 0; i < Settings.populationSize; i++) {
            newPopulation.add(new SPEA2Chromosome(mstChromosome));
        }
        return newPopulation;
    }

    private void assignFitness(ArrayList<SPEA2Chromosome> chromosomes) {

    }

    private ArrayList<SPEA2Chromosome> environmentalSelection() {
        ArrayList<SPEA2Chromosome> newArchive = new ArrayList<>(archive);
        newArchive.addAll(getAllNonDominated(population));
        return newArchive;
    }

    private ArrayList<SPEA2Chromosome> getAllNonDominated(ArrayList<SPEA2Chromosome> chromosomes) {
        ArrayList<SPEA2Chromosome> nonDominated = new ArrayList<>();
        for (SPEA2Chromosome c1 : chromosomes) {
            boolean dominated = false;
            for (SPEA2Chromosome c2 : chromosomes) {
                if (c2.dominates(c1)) {
                    dominated = true;
                    break;
                }
            }
            if (!dominated) nonDominated.add(c1);
        }
        return nonDominated;
    }



}
