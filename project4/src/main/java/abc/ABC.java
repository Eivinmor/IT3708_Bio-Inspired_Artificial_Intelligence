package abc;

import representation.JSP;
import utility.Tools;

import java.util.ArrayList;
import java.util.Collections;

public class ABC {

    private int[][] foodSources;
    private int[] bestFoodSource, selectedFoodSources, roundsSinceImprovement;
    private ABCSolution bestSolution;
    private ABCSolution[] solutions;

    public void runAlgorithm() {
        // First step
        foodSources = constructInitialFoodSources();
        bestFoodSource = foodSources[0].clone();
        bestSolution = new ABCSolution(foodSources[0]);
        solutions = new ABCSolution[Settings.employed];
        roundsSinceImprovement = new int[foodSources.length];


        for (int i = 0; i < Settings.rounds; i++) {
            for (int j = 0; j < roundsSinceImprovement.length; j++) roundsSinceImprovement[j]++;
            // Second step
            updateAllFoodSources();
            // Third step
            selectFoodSources();
            replaceBestFoodSource();
            updateSelectedFoodSources();
            // Fourth step
            replaceStagnatedFoodSources(i);
            // Fifth step
            bestFoodSource = variableNeighbourSearch(bestFoodSource);
//            variableNeighbourSearch(bestFoodSource);
            bestSolution = new ABCSolution(bestFoodSource);
            System.out.println(bestSolution.makespan);
        }
        Tools.plotter.plotSolution(bestSolution);
    }

    private int[][] constructInitialFoodSources() {
        ArrayList<Integer> jobs = new ArrayList<>(JSP.numOfOperations);
        for (int i = 0; i < JSP.numOfJobs; i++) {
            for (int j = 0; j < JSP.numOfMachines; j++) jobs.add(i);
        }
        int[][] foodSources = new int[Settings.employed][JSP.numOfOperations];
        for (int i = 0; i < Settings.employed; i++) {
            int[] foodSource = new int[JSP.numOfOperations];
            Collections.shuffle(jobs);
            for (int j = 0; j < JSP.numOfOperations; j++) {
                foodSource[j] = jobs.get(j);
            }
            foodSources[i] = foodSource;
        }
        return foodSources;
    }
    
    private void updateAllFoodSources() {
        for (int i = 0; i < Settings.employed; i++) solutions[i] = new ABCSolution(foodSources[i]);
        for (int i = 0; i < foodSources.length; i++) {
            int[] p1 = foodSources[i];
            int[] p2 = foodSources[Tools.random.nextInt(foodSources.length)];
            int[] c = crossover(p1, p2);
            ABCSolution cSolution = new ABCSolution(c);
            if (cSolution.makespan < solutions[i].makespan) {
                foodSources[i] = c;
                solutions[i] = cSolution;
                roundsSinceImprovement[i] = 0;
            }
        }
    }

    private void selectFoodSources() {
        int[] selected = new int[Settings.onlookers];
        double totalFitness = 0;
        for (int i = 0; i < Settings.employed; i++) {
            totalFitness += 1/solutions[i].makespan;
        }
        for (int i = 0; i < Settings.onlookers; i++) {
            double randomValue = Tools.random.nextDouble() * totalFitness;
            for (int j = 0; j < foodSources.length; j++) {
                randomValue -= 1 / solutions[j].makespan;
                if (randomValue <= 0) {
                    selected[i] = j;
                    break;
                }
            }
        }
        selectedFoodSources = selected;
    }

    private void replaceBestFoodSource() {
        int[] curBestFoodSource = foodSources[selectedFoodSources[0]];
        ABCSolution curBestSolution = new ABCSolution(curBestFoodSource);

        for (int i = 1; i < selectedFoodSources.length; i++) {
            int[] curSelectedFoodSource = foodSources[selectedFoodSources[i]];
            ABCSolution solution = solutions[selectedFoodSources[i]];
            if (solution.makespan < curBestSolution.makespan) {
                curBestFoodSource = curSelectedFoodSource;
                curBestSolution = solution;
            }
        }
        curBestFoodSource = variableNeighbourSearch(curBestFoodSource);
        curBestSolution = new ABCSolution(curBestFoodSource);
        if (curBestSolution.makespan < bestSolution.makespan) {
            bestFoodSource = curBestFoodSource;
            bestSolution = curBestSolution;
        }
    }

    private void updateSelectedFoodSources() {
        for (int i = 0; i < selectedFoodSources.length; i++) {
            int foodSourceIndex = selectedFoodSources[i];
            int[] p1 = foodSources[foodSourceIndex];
            int[] c = crossover(p1, bestFoodSource);
            ABCSolution cSolution = new ABCSolution(c);
            if (cSolution.makespan < solutions[foodSourceIndex].makespan) {
                foodSources[foodSourceIndex] = c;
                solutions[foodSourceIndex] = cSolution;
                roundsSinceImprovement[foodSourceIndex] = 0;
            }
        }
    }

    private void replaceStagnatedFoodSources(int round) {
        for (int i = 0; i < foodSources.length; i++) {
            if (roundsSinceImprovement[i] > Settings.roundsBeforeScouting) {

                int[] newFoodSource = new int[JSP.numOfOperations];
                ArrayList<Integer> selected = new ArrayList<>(JSP.numOfOperations);

                double prob = Settings.omegaMax -
                        (((double) round / Settings.rounds) * (Settings.omegaMax - Settings.omegaMin));

                for (int j = 0; j < JSP.numOfOperations; j++) {
                    if (Tools.random.nextDouble() < prob) {
                        selected.add(j);
                    }
                    else newFoodSource[j] = foodSources[i][j];
                }

                ArrayList<Integer> shuffledSelected = new ArrayList<>(selected);
                Collections.shuffle(shuffledSelected);

                for (int j = 0; j < selected.size(); j++) {
                    newFoodSource[selected.get(j)] = foodSources[i][shuffledSelected.get(j)];
                }
                foodSources[i] = newFoodSource;
                solutions[i] = new ABCSolution(newFoodSource);
                roundsSinceImprovement[i] = 0;
            }
        }
    }


    private int[] crossover(int[] p1, int[] p2) {
        ArrayList<Integer> availableIndexes = new ArrayList<>(JSP.numOfOperations);
        int[] jobCount = new int[JSP.numOfJobs];
        int[] s = new int[JSP.numOfOperations];
        for (int i = 0; i < JSP.numOfOperations; i++) {
            if (Tools.random.nextBoolean()) {
                s[i] = p1[i];
                jobCount[s[i]]++;
            }
            else availableIndexes.add(i);
        }
        for (int i = 0; i < JSP.numOfOperations; i++) {
            if (jobCount[p2[i]] < JSP.numOfMachines) {
                s[availableIndexes.remove(0)] = p2[i];
                jobCount[p2[i]]++;
            }
        }
        return s;
    }
    
    private int[] variableNeighbourSearch(int[] foodSource) {
        ABCSolution initialSolution = new ABCSolution(foodSource);
        int[] firstFoodSource = foodSource.clone();
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
            if (new ABCSolution(secondFoodSource).makespan < new ABCSolution(firstFoodSource).makespan) {
                firstFoodSource = secondFoodSource.clone();
            }
            else {
                p = Math.abs(p - 1);
            }
        }
        if (new ABCSolution(firstFoodSource).makespan < initialSolution.makespan) {
            return firstFoodSource.clone();
        }
        return foodSource;
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
