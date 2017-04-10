package pso;


import representation.JSP;
import utility.Tools;

import java.util.ArrayList;
import java.util.Arrays;
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

        if (velocityMatrix[machine][job] <= 0
                && velocityMatrix[machine][j1SolutionLocation] <= 0
                && j1 != j2) {
            preferenceMatrix[machine][job] = j2;
            preferenceMatrix[machine][j1SolutionLocation] = j1;
            velocityMatrix[machine][job] = 1;
            // TODO Not change velocity of job2?
//            velocityMatrix[machine][job] = Settings.movementDelay;
        }
        if (Tools.random.nextDouble() <= Settings.mutationRate) mutate();
    }

    void mutate() {
        int machine = Tools.random.nextInt(JSP.numOfMachines);
        int job1 = Tools.random.nextInt(JSP.numOfJobs);
        int job2 = Tools.random.nextInt(JSP.numOfJobs);
        int pref1 = preferenceMatrix[machine][job1];
        int pref2 = preferenceMatrix[machine][job1];
        preferenceMatrix[machine][job1] = pref2;
        preferenceMatrix[machine][job2] = pref1;
        velocityMatrix[machine][job1] = 1;
        velocityMatrix[machine][job2] = 1;
    }


//    public int[][] generateSchedule() {
//        // TODO Kan optimaliseres uten HashSet
//
//        int[][] schedule = new int[JSP.numOfMachines][JSP.numOfJobs];
//        ArrayList<Operation> schedulable = new ArrayList<>(JSP.numOfMachines);
//        double[] jobEndTime = new double[JSP.numOfJobs];
//        double[] machineEndTime = new double[JSP.numOfMachines];
//        int[] machineOperations = new int[JSP.numOfMachines];
//
//        @SuppressWarnings("unchecked")
//        ArrayList<Operation>[] jobQueues = new ArrayList[JSP.numOfJobs];
//        for (int i = 0; i < jobQueues.length; i++) {
//            jobQueues[i] = new ArrayList<>(Arrays.asList(JSP.jobs[i]));
//            schedulable.add(jobQueues[i].remove(0));
//        }
//        while (!schedulable.isEmpty()) {
//            // Find best machine
//            Operation earliestDoneOperation = schedulable.get(0);
//            double bestFinishTime = jobEndTime[earliestDoneOperation.job] + earliestDoneOperation.duration;
//            for (int i = 1; i < schedulable.size(); i++) {
//                Operation op = schedulable.get(i);
//                double opFinisTime = Math.max(jobEndTime[op.job], machineEndTime[op.machine]) + op.duration;
//                if (opFinisTime < bestFinishTime) {
//                    earliestDoneOperation = op;
//                    bestFinishTime = opFinisTime;
//                }
//            }
//            // Find highest prio within machine of best
//            int bestMachine = earliestDoneOperation.machine;
//            Operation prioOp = earliestDoneOperation;
//            for (Operation op : schedulable) {
//                if (op.machine == bestMachine
//                        && preferenceMatrix[op.machine][op.job] < preferenceMatrix[prioOp.machine][prioOp.job])
//                    prioOp = op;
//            }
//            double prioOpFinishTime = Math.max(jobEndTime[prioOp.job], machineEndTime[prioOp.machine]) + prioOp.duration;
//            schedulable.remove(prioOp);
//            if (!jobQueues[prioOp.job].isEmpty()) schedulable.add(jobQueues[prioOp.job].remove(0));
//            schedule[prioOp.machine][prioOp.job] = machineOperations[prioOp.machine];
//            machineOperations[prioOp.machine]++;
//            jobEndTime[prioOp.job] = prioOpFinishTime;
//            machineEndTime[prioOp.machine] = prioOpFinishTime;
//            int counter = 0;
//            for (Operation op : JSP.jobs[prioOp.job]) {
//                if (op.machine == prioOp.machine)
//                    operationStartTimes[prioOp.job][counter] = prioOpFinishTime - prioOp.duration;
//                counter++;
//            }
////            operationStartTimes[prioOp.job][prioOp.machine] = prioOpFinishTime - prioOp.duration;
////            System.out.println(prioOp);
//        }
//        double maxSpan = machineEndTime[0];
//        for (int i = 1; i < machineEndTime.length; i++)
//            if (machineEndTime[i] > maxSpan) maxSpan = machineEndTime[i];
//        makespan = maxSpan;
//        return schedule;
//    }
}

