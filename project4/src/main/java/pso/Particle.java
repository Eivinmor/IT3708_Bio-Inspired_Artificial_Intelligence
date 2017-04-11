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
        ArrayList<Integer> jobsIds = new ArrayList<>(JSP.numOfJobs);
        for (int i = 0; i < JSP.numOfJobs; i++) jobsIds.add(i);
        for (int i = 0; i < JSP.numOfMachines; i++) {
            Collections.shuffle(jobsIds);
            for (int j = 0; j < jobsIds.size(); j++) preferenceMatrix[i][j] = jobsIds.get(j);
        }
    }

    // TODO - Test
    void moveToward(int machine, int l, Solution solution) {
        int j1 = preferenceMatrix[machine][l];

        int j1SolutionLocation = -1;
        for (int i = 0; i < JSP.numOfJobs; i++) {
            if (solution.schedule[machine][i] == j1) j1SolutionLocation = i;
        }
        int j2 = preferenceMatrix[machine][j1SolutionLocation];

        if (velocityMatrix[machine][j1] == 0 && velocityMatrix[machine][j2] == 0 && j1 != j2) {
            preferenceMatrix[machine][l] = j2;
            preferenceMatrix[machine][j1SolutionLocation] = j1;
            // TODO Not change velocity of job2?
            velocityMatrix[machine][j1] = 1;
        }
    }

    void mutate() {
        int machine = Tools.random.nextInt(JSP.numOfMachines);
        int l1 = Tools.random.nextInt(JSP.numOfJobs);
        int l2 = Tools.random.nextInt(JSP.numOfJobs);
        int j1 = preferenceMatrix[machine][l1];
        int j2 = preferenceMatrix[machine][l2];
        preferenceMatrix[machine][l1] = j2;
        preferenceMatrix[machine][l2] = j1;
        velocityMatrix[machine][j1] = 1;
        velocityMatrix[machine][j2] = 1;
    }

}

