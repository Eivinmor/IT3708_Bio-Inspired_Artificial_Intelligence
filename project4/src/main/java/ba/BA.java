package ba;

import representation.JSP;
import utility.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BA {

    private int[][] foodSources;
    private int[] bestFoodSource, selectedFoodSources, roundsSinceImprovement;
    private BASolution bestSolution;
    private BASolution[] solutions;

    public void runAlgorithm() {
        // First step
        foodSources = constructInitialFoodSources();
        bestFoodSource = foodSources[0].clone();
        bestSolution = new BASolution(foodSources[0]);
        solutions = new BASolution[Settings.employed];
        roundsSinceImprovement = new int[foodSources.length];


        for (int i = 0; i < Settings.rounds; i++) {
            for (int j = 0; j < roundsSinceImprovement.length; j++) roundsSinceImprovement[j]++;
            // Second step
            updateAllFoodSources();
            // Third step
            selectFoodSources();
            replaceBestFoodSource();
            updateSelectedFoodSources();
        }
        System.out.println(Arrays.toString(roundsSinceImprovement));
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
        for (int i = 0; i < Settings.employed; i++) solutions[i] = new BASolution(foodSources[i]);
        for (int i = 0; i < foodSources.length; i++) {
            int[] p1 = foodSources[i];
            int[] p2 = foodSources[Tools.random.nextInt(foodSources.length)];
            int[] c = crossover(p1, p2);
            BASolution cSolution = new BASolution(c);
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
        for (int i = 0; i < selectedFoodSources.length; i++) {
            BASolution solution = solutions[selectedFoodSources[i]];
            if (solution.makespan < bestSolution.makespan) {
                bestSolution = solution;
                bestFoodSource = foodSources[selectedFoodSources[i]];
            }
        }
    }

    private void updateSelectedFoodSources() {
        for (int i = 0; i < selectedFoodSources.length; i++) {
            int foodSourceIndex = selectedFoodSources[i];
            int[] p1 = foodSources[foodSourceIndex];
            int[] c = crossover(p1, bestFoodSource);
            BASolution cSolution = new BASolution(c);
            if (cSolution.makespan < solutions[foodSourceIndex].makespan) {
                foodSources[foodSourceIndex] = c;
                solutions[foodSourceIndex] = cSolution;
                roundsSinceImprovement[foodSourceIndex] = 0;
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

}
