package ga;


import representation.Grid;

import java.util.ArrayList;

public class SimpleGA {
    private Grid grid;

    // TODO Mutation - tumour (dumb)
    // TODO Mutation - loop outside border, add if closer to this (smart)
    // TODO Mutation - merge closest (color dist) adjacent segment (smart)

    // TODO Crossover - half and half on x, y and diagonal
    // TODO Crossover - ta noen segmenter fra p1 og legg inn i p2 (overlapper de som er)


    // TODO Initial population
    // TODO Elitism
    // TODO Parent selection
    // Crossover
    // Mutation
    // TODO Survival selection

    public SimpleGA(Grid grid) {
        this.grid = grid;
    }

    public void runAlgorithm() {
        ArrayList<Chromosome> population = generateInitialPopulation();

    }

    public ArrayList<Chromosome> generateInitialPopulation() {
        ArrayList<Chromosome> initPopulation = new ArrayList<>(Settings.populationSize);

        for (int i = 0; i < Settings.populationSize; i++) {
            initPopulation.add(new Chromosome(grid));
        }

        return initPopulation;
    }




}
