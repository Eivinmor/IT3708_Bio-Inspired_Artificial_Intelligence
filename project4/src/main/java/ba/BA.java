package ba;

import representation.JSP;
import utility.Tools;

import java.util.ArrayList;
import java.util.Collections;

public class BA {

    public void runAlgorithm() {

        int[][] foodSources = constuctInitialFoodSources();
        int[] bestFoodSource = foodSources[0].clone();
        BASolution bestSolution = new BASolution(foodSources[0]);
        BASolution[] solutions = new BASolution[Settings.employed];

        for (int i = 0; i < Settings.rounds; i++) {
            updateSolutions(foodSources, solutions);
            selectFoodSources(foodSources, solutions);
        }

    }

    private int[][] constuctInitialFoodSources() {
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
    
    private void updateSolutions(int[][] foodSources, BASolution[] solutions) {
        for (int i = 0; i < Settings.employed; i++) solutions[i] = new BASolution(foodSources[i]);
        for (int i = 0; i < foodSources.length; i++) {
            int[] p1 = foodSources[i];
            int[] p2 = foodSources[Tools.random.nextInt(foodSources.length)];
            int[] c = crossover(p1, p2);
            BASolution cSolution = new BASolution(c);
            if (cSolution.makespan < solutions[i].makespan) {
                foodSources[i] = c;
                solutions[i] = cSolution;
            }
        }
    }

    private int[] selectFoodSources(int[][] foodSources, BASolution[] solutions) {
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
        return selected;
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
