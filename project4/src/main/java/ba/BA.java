package ba;


import representation.JSP;
import utility.Tools;

import java.util.ArrayList;
import java.util.Collections;

public class BA {

    public void runAlgorithm() {

        // Initiate scouted solutions (generate random solutions)
        ArrayList<BASolution> population = scatterScouts(Settings.numOfScoutBees);
        ArrayList<BASolution> nextPopulation;
        BASolution bestSolution = population.get(0);

        // START LOOP
        while (true) {
            nextPopulation = new ArrayList<>(Settings.populationSize);

            // Sort scouted sites
            Collections.sort(population);

            // Replace best solution
            if (population.get(0).makespan < bestSolution.makespan) {
                bestSolution = population.get(0);
            }

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
            ArrayList<BASolution> nextPopulationAfterAbandonment = new ArrayList<>(Settings.populationSize);
            for (BASolution site : nextPopulation) {
                if (++site.roundsWithoutImprovement <= Settings.numOfStagnationRoundsBeforeAbandonment) {
                   nextPopulationAfterAbandonment.add(site);
                }
            }
            nextPopulation = nextPopulationAfterAbandonment;

            // Scout new sites
            nextPopulation.addAll
                    (scatterScouts(Settings.populationSize - nextPopulation.size()));

            population = new ArrayList<>(nextPopulation);
            bestSolution = variableNeighbourSearch(bestSolution);
            System.out.println(bestSolution.makespan);
            Tools.plotter.plotSolution(bestSolution);
        }
    }

    private ArrayList<BASolution> scatterScouts(int numberOfScouts) {
        ArrayList<Integer> jobs = new ArrayList<>(JSP.numOfOperations);
        for (int i = 0; i < JSP.numOfJobs; i++) {
            for (int j = 0; j < JSP.numOfMachines; j++) jobs.add(i);
        }
        ArrayList<BASolution> solutions = new ArrayList<>(numberOfScouts);
        for (int i = 0; i < numberOfScouts; i++) {
            int[] foodSource = new int[JSP.numOfOperations];
            Collections.shuffle(jobs);
            for (int j = 0; j < JSP.numOfOperations; j++) {
                foodSource[j] = jobs.get(j);
            }
            solutions.add(new BASolution(foodSource));
        }
        return solutions;
    }

    private BASolution searchNeighbourhood(BASolution solution, int numOfBees) {
        boolean changed = false;
        BASolution bestNeighbourhoodSolution = solution;
        for (int i = 0; i < numOfBees; i++) {
            BASolution neighbourSolution = getNeighbourSolution(solution);
            if (neighbourSolution.makespan <= bestNeighbourhoodSolution.makespan) {
                bestNeighbourhoodSolution = neighbourSolution;
                if (neighbourSolution.makespan < bestNeighbourhoodSolution.makespan) changed = true;
            }
        }
        if (changed) bestNeighbourhoodSolution.neighbourhoodSize = solution.neighbourhoodSize;
        else {
            bestNeighbourhoodSolution.neighbourhoodSize *= (1 - Settings.neighbourhoodReduction);
        }
        return variableNeighbourSearch(bestNeighbourhoodSolution);
    }

    private BASolution getNeighbourSolution(BASolution solution) {
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
        return new BASolution(newFoodSource);
    }


    private BASolution variableNeighbourSearch(BASolution initialSolution) {
        int[] firstFoodSource = initialSolution.foodSource.clone();
        int p = 1;

        int alpha = Tools.random.nextInt(JSP.numOfOperations);
        int beta = Tools.random.nextInt(JSP.numOfOperations);
        while (beta == alpha) beta = Tools.random.nextInt(JSP.numOfOperations);
        firstFoodSource = exchangingProcess(firstFoodSource, alpha, beta);

        alpha = Tools.random.nextInt(JSP.numOfOperations);
        beta = Tools.random.nextInt(JSP.numOfOperations);
        while (beta == alpha) beta = Tools.random.nextInt(JSP.numOfOperations);
        firstFoodSource = insertingProcess(firstFoodSource, alpha, beta);

        alpha = Tools.random.nextInt(JSP.numOfOperations);
        beta = Tools.random.nextInt(JSP.numOfOperations);
        while (beta == alpha) beta = Tools.random.nextInt(JSP.numOfOperations);
        firstFoodSource = exchangingProcess(firstFoodSource, alpha, beta);

        for (int i = 0; i <= (JSP.numOfOperations * (JSP.numOfOperations - 1)); i++) {
            int[] secondFoodSource;
            alpha = Tools.random.nextInt(JSP.numOfOperations);
            beta = Tools.random.nextInt(JSP.numOfOperations);
            while (beta == alpha) beta = Tools.random.nextInt(JSP.numOfOperations);
            if (p == 1) {
                secondFoodSource = exchangingProcess(firstFoodSource, alpha, beta);
            }
            else {
                secondFoodSource = insertingProcess(firstFoodSource, alpha, beta);
            }
            if (new BASolution(secondFoodSource).makespan < new BASolution(firstFoodSource).makespan) {
                firstFoodSource = secondFoodSource.clone();
            }
            else {
                p = Math.abs(p - 1);
            }
        }
        BASolution firstFoodSourceSolution = new BASolution(firstFoodSource);
        if (firstFoodSourceSolution.makespan < initialSolution.makespan) {
            return firstFoodSourceSolution;
        }
        return initialSolution;
    }

    private int[] exchangingProcess(int[] foodSource, int alpha, int beta) {
        int[] newFoodSource = foodSource.clone();
        newFoodSource[alpha] = foodSource[beta];
        newFoodSource[beta] = foodSource[alpha];
        return newFoodSource;
    }

    private int[] insertingProcess(int[] foodSource, int alpha, int beta) {
        int[] newFoodSource = foodSource.clone();
        if (alpha > beta) {
            for (int i = beta; i < alpha; i++) {
                newFoodSource[i+1] = foodSource[i];
            }
        }
        else {
            for (int i = alpha; i < foodSource.length - 1; i++) {
                if (i == beta) break;
                newFoodSource[i] = foodSource[i + 1];
            }
        }
        newFoodSource[beta] = foodSource[alpha];
        return newFoodSource;
    }

}
