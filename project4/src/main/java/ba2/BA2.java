package ba2;


import representation.JSP;
import utility.Tools;

import java.util.ArrayList;
import java.util.Collections;

public class BA2 {

    double neighbourhoodSize = Settings.initialNeighbourhoodSize;

    public void runAlgorithm() {

        // Initiate scouted solutions (generate random solutions)
        ArrayList<BA2Solution> population = scatterScouts(Settings.populationSize);
        ArrayList<BA2Solution> nextPopulation;
        BA2Solution bestSolution = population.get(0);

        // START LOOP
        while (true) {
            // Sort scouted sites (generate random solutions)
            Collections.sort(population);
            nextPopulation = new ArrayList<>(Settings.populationSize);
            neighbourhoodSize *= (1 - Settings.neighbourhoodReduction);
            if (population.get(0).makespan < bestSolution.makespan) bestSolution = population.get(0);

            // Present nb best of the scouted sites
            for (int i = 0; i < Settings.numOfEliteSites; i++) {
                // Elite search
                nextPopulation.add(searchNeighbourhood(population.get(i), Settings.beesPerEliteSite));
            }
            for (int i = Settings.numOfEliteSites; i < Settings.numOfBestSites; i++) {
                // Best search
                nextPopulation.add(searchNeighbourhood(population.get(i), Settings.beesPerBestSite));
            }
            // Site abandonment
            ArrayList<BA2Solution> nextPopulationAfterAbandonment = new ArrayList<>(Settings.populationSize);
            for (BA2Solution site : nextPopulation) {
                if (site.roundsWithoutImprovement >= Settings.numOfStagnationRoundsBeforeAbandonment) {
                   nextPopulationAfterAbandonment.add(site);
                }
            }
            nextPopulation = nextPopulationAfterAbandonment;

            // Scout new sites
            nextPopulation.addAll
                    (scatterScouts(Settings.populationSize - nextPopulation.size()));

            population = nextPopulation;
            System.out.println(bestSolution.makespan);
            Tools.plotter.plotSolution(bestSolution);
        }
    }

    private ArrayList<BA2Solution> scatterScouts(int numberOfScouts) {
        ArrayList<Integer> jobs = new ArrayList<>(JSP.numOfOperations);
        for (int i = 0; i < JSP.numOfJobs; i++) {
            for (int j = 0; j < JSP.numOfMachines; j++) jobs.add(i);
        }
        ArrayList<BA2Solution> solutions = new ArrayList<>(numberOfScouts);
        for (int i = 0; i < Settings.numOfScoutBees; i++) {
            int[] foodSource = new int[JSP.numOfOperations];
            Collections.shuffle(jobs);
            for (int j = 0; j < JSP.numOfOperations; j++) {
                foodSource[j] = jobs.get(j);
            }
            solutions.add(new BA2Solution(foodSource));
        }
        return solutions;
    }

    private BA2Solution searchNeighbourhood(BA2Solution solution, int numOfBees) {
        // Shrink neighbourhoods
        BA2Solution bestNeighbourhoodSolution = null;
        for (int i = 0; i < numOfBees; i++) {

        }
        return solution;
    }

}
