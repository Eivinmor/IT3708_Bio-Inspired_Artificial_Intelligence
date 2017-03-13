//package ga;
//
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashSet;
//
//public class SimpleGA {
//
//    // TODO Mutation - tumour (dumb)
//    // TODO Mutation - loop outside border, add if closer to this (smart)
//    // TODO Mutation - merge closest (color dist) adjacent segment (smart)
//
//    // TODO Crossover - half and half on x, y and diagonal
//    // TODO Crossover - ta noen segmenter fra p1 og legg inn i p2 (overlapper de som er)
//
//
//    // TODO Initial population
//    // TODO Elitism
//    // TODO Parent selection
//    // Crossover
//    // Mutation
//    // TODO Survival selection
//
//    public SimpleGA() {
//    }
//
//    public void runAlgorithm() {
//        ArrayList<Chromosome> population = generateInitialPopulation();
//        ArrayList<Chromosome> nextPopulation = new ArrayList<>(Settings.populationSize);
//        nextPopulation.addAll(eliteSelection(population));
//
//    }
//
//    private ArrayList<Chromosome> generateInitialPopulation() {
//        ArrayList<Chromosome> initPopulation = new ArrayList<>(Settings.populationSize);
//        for (int i = 0; i < Settings.populationSize; i++) {
//            initPopulation.add(new Chromosome());
//        }
//        return initPopulation;
//    }
//
//    private HashSet<Chromosome> eliteSelection(ArrayList<Chromosome> population) {
//        HashSet<Chromosome> eliteSet = new HashSet<>(1 + Settings.eliteSize);
//        Collections.sort(population);
//        eliteSet.add(new Chromosome(population.get(0)));
//        for (int i = 0; i < Settings.eliteSize; i++) {
//            Chromosome eliteChromosome = new Chromosome(population.get(i));
//            eliteChromosome.mutate();
//            eliteSet.add(eliteChromosome);
//        }
//        return eliteSet;
//    }
//
//}
