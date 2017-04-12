package pso;


import representation.JSP;
import utility.Tools;

import java.util.ArrayList;
import java.util.Collections;


public class Particle {

    int[][] preferenceMatrix;
    int[][] velocityMatrix;

    Particle() {
        preferenceMatrix = new int[JSP.numOfMachines][JSP.numOfJobs];
        velocityMatrix = new int[JSP.numOfMachines][JSP.numOfJobs];
        ArrayList<Integer> preferences = new ArrayList<>(JSP.numOfJobs);
        for (int i = 0; i < JSP.numOfJobs; i++) preferences.add(i);
        for (int i = 0; i < JSP.numOfMachines; i++) {
            Collections.shuffle(preferences);
            for (int j = 0; j < preferences.size(); j++) preferenceMatrix[i][j] = preferences.get(j);
        }
    }

    // TODO - Test
    void moveToward(int machine, int job, Solution solution) {
        int j1 = preferenceMatrix[machine][job];

        int j1SolutionLocation = -1;
        for (int i = 0; i < JSP.numOfJobs; i++) {
            if (solution.schedule[machine][i] == j1) j1SolutionLocation = i;
        }
        int j2 = preferenceMatrix[machine][j1SolutionLocation];

        if (velocityMatrix[machine][job] == 0  && velocityMatrix[machine][j1SolutionLocation] == 0 && j1 != j2) {
            preferenceMatrix[machine][job] = j2;
            preferenceMatrix[machine][j1SolutionLocation] = j1;
            velocityMatrix[machine][job] = 1;
        }
    }

    void mutate() {
        int machine = Tools.random.nextInt(JSP.numOfMachines);
        int job1 = Tools.random.nextInt(JSP.numOfJobs);
        int job2 = Tools.random.nextInt(JSP.numOfJobs);
        int pref1 = preferenceMatrix[machine][job1];
        int pref2 = preferenceMatrix[machine][job2];
        preferenceMatrix[machine][job1] = pref2;
        preferenceMatrix[machine][job2] = pref1;
        velocityMatrix[machine][job1] = 1;
        velocityMatrix[machine][job2] = 1;
    }
}

