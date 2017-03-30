package pso;


import representation.JSP;
import representation.Operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class Particle {

    public int[][] preferenceMatrix;

    public Particle() {
        preferenceMatrix = new int[JSP.numOfMachines][JSP.numOfJobs];
        ArrayList<Integer> preferences = new ArrayList<>(JSP.numOfJobs);
        for (int i = 0; i < JSP.numOfJobs; i++) preferences.add(i);
        for (int i = 0; i < JSP.numOfMachines; i++) {
            Collections.shuffle(preferences);
            for (int j = 0; j < preferences.size(); j++) preferenceMatrix[i][j] = preferences.get(j);
        }
    }


    public int[][] generateSchedule() {
        // TODO Kan optimaliseres uten HashSet

        int[][] schedule = new int[JSP.numOfMachines][JSP.numOfJobs];
        ArrayList<Operation> schedulable = new ArrayList<>(JSP.numOfMachines);
        double[] jobEndTime = new double[JSP.numOfJobs];
        int[] machineOperations = new int[JSP.numOfMachines];

        @SuppressWarnings("unchecked")
        ArrayList<Operation>[] jobQueues = new ArrayList[JSP.numOfJobs];
        for (int i = 0; i < jobQueues.length; i++) {
            jobQueues[i] = new ArrayList<>(Arrays.asList(JSP.jobs[i]));
            schedulable.add(jobQueues[i].remove(0));
        }
        while (!schedulable.isEmpty()) {
            Operation bestOperation = schedulable.get(0);
            double bestFinishTime = jobEndTime[bestOperation.job] + bestOperation.duration;
            for (int i = 1; i < schedulable.size(); i++) {
                Operation op = schedulable.get(i);
                double opFinisTime = jobEndTime[op.job] + op.duration;
                if (opFinisTime < bestFinishTime) {
                    bestOperation = op;
                    bestFinishTime = opFinisTime;
                }
            }
            schedulable.remove(bestOperation);
            if (!jobQueues[bestOperation.job].isEmpty()) schedulable.add(jobQueues[bestOperation.job].remove(0));
            schedule[bestOperation.machine][bestOperation.job] = machineOperations[bestOperation.machine];
            machineOperations[bestOperation.machine]++;
        }
        return schedule;
    }




}

