package ba2;


import representation.JSP;
import utility.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class BA2 {

    public void runAlgorithm() {

        // Initiate scouted solutions (generate random solutions)
        ArrayList<BA2Solution> population = scatterScouts(Settings.numOfScoutBees);
        ArrayList<BA2Solution> nextPopulation;
        BA2Solution bestSolution = population.get(0);

        // START LOOP
        while (true) {
            nextPopulation = new ArrayList<>(Settings.populationSize);

            // Sort scouted sites
            Collections.sort(population);

            // Replace best solution
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
                if (++site.roundsWithoutImprovement <= Settings.numOfStagnationRoundsBeforeAbandonment) {
                   nextPopulationAfterAbandonment.add(site);
                }
            }
            nextPopulation = nextPopulationAfterAbandonment;

            // Scout new sites
            nextPopulation.addAll
                    (scatterScouts(Settings.populationSize - nextPopulation.size()));

            population = new ArrayList<>(nextPopulation);
            System.out.println(bestSolution.makespan);

//            System.out.println();
//            for (BA2Solution s : population) {
//                System.out.print(s.neighbourhoodSize + " ");
//            }
//            System.out.println();
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    private ArrayList<BA2Solution> scatterScouts(int numberOfScouts) {
        ArrayList<Integer> jobs = new ArrayList<>(JSP.numOfOperations);
        for (int i = 0; i < JSP.numOfJobs; i++) {
            for (int j = 0; j < JSP.numOfMachines; j++) jobs.add(i);
        }
        ArrayList<BA2Solution> solutions = new ArrayList<>(numberOfScouts);
        for (int i = 0; i < numberOfScouts; i++) {
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
        boolean changed = false;
        BA2Solution bestNeighbourhoodSolution = solution;
        for (int i = 0; i < numOfBees; i++) {
            BA2Solution neighbourSolution = getNeighbourSolution(solution);
            if (neighbourSolution.makespan <= bestNeighbourhoodSolution.makespan) {
                bestNeighbourhoodSolution = neighbourSolution;
                changed = true;
            }
        }
        if (changed) bestNeighbourhoodSolution.neighbourhoodSize = solution.neighbourhoodSize;
        else {
            bestNeighbourhoodSolution.neighbourhoodSize *= (1 - Settings.neighbourhoodReduction);
        }
//        System.out.println(bestNeighbourhoodSolution.neighbourhoodSize);
        return bestNeighbourhoodSolution;
    }

    private BA2Solution getNeighbourSolution(BA2Solution solution) {
        int[] newFoodSource = new int[JSP.numOfOperations];
        ArrayList<Integer> selectedIndexes = new ArrayList<>(JSP.numOfOperations);
        for (int i = 0; i < JSP.numOfOperations; i++) {
            if (Tools.random.nextDouble() <= solution.neighbourhoodSize) {
                selectedIndexes.add(i);
            }
            else newFoodSource[i] = solution.foodSource[i];
        }
        ArrayList<Integer> shuffledSelected = new ArrayList<>(selectedIndexes);
        Collections.shuffle(shuffledSelected);

        for (int i = 0; i < selectedIndexes.size(); i++) {
            newFoodSource[selectedIndexes.get(i)] = solution.foodSource[shuffledSelected.get(i)];
        }
        return new BA2Solution(newFoodSource);
    }

}
