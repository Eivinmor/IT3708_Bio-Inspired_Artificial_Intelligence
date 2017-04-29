package ba;

import representation.JSP;
import utility.Tools;

import java.util.ArrayList;
import java.util.Collections;

public class BA {

    public void runAlgorithm() {
//
//        JSP.numOfJobs = 3;
//        JSP.numOfMachines = 3;
//        JSP.numOfOperations = 9;
//
//        int[] s1 = {1, 2, 0, 2, 0, 1, 0, 1, 2};
//        int[] s2 = {0, 1, 2, 0, 2, 1, 2, 1, 0};

//        crossover(s1, s2);

        constructInitialSolutions();
        for (int i = 0; i < Settings.rounds; i++) {
            
        }
    }

    private int[][] constructInitialSolutions() {
        ArrayList<Integer> jobs = new ArrayList<>(JSP.numOfOperations);
        for (int i = 0; i < JSP.numOfJobs; i++) {
            for (int j = 0; j < JSP.numOfMachines; j++) jobs.add(i);
        }
        int[][] solutions = new int[Settings.employed][JSP.numOfOperations];
        for (int i = 0; i < Settings.employed; i++) {
            int[] solution = new int[JSP.numOfOperations];
            Collections.shuffle(jobs);
            for (int j = 0; j < JSP.numOfOperations; j++) {
                solution[j] = jobs.get(j);
            }
            solutions[i] = solution;
        }
        return solutions;
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
