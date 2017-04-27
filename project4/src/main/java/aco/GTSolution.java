package aco;

import representation.JSP;
import representation.Operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


public class GTSolution {

    final double makespan;
    public double[][] operationStartTimes;

    GTSolution(Ant ant) {
        ArrayList<Operation> schedulable = new ArrayList<>(JSP.numOfMachines);
        double[] jobEndTime = new double[JSP.numOfJobs];
        double[] machineEndTime = new double[JSP.numOfMachines];
        operationStartTimes = new double[JSP.numOfJobs][JSP.numOfMachines];

        int[][] preferenceMatrix = antPathToPreferenceMatrix(ant.path);

        @SuppressWarnings("unchecked")
        ArrayList<Operation>[] jobQueues = new ArrayList[JSP.numOfJobs];
        for (int i = 0; i < jobQueues.length; i++) {
            jobQueues[i] = new ArrayList<>(Arrays.asList(JSP.jobs[i]));
            schedulable.add(jobQueues[i].remove(0));
        }
        while (!schedulable.isEmpty()) {
            // Find best machine
            Operation earliestDoneOperation = schedulable.get(0);
            double bestFinishTime =
                    Math.max(jobEndTime[earliestDoneOperation.job], machineEndTime[earliestDoneOperation.machine])
                            + earliestDoneOperation.duration;
            for (int i = 1; i < schedulable.size(); i++) {
                Operation op = schedulable.get(i);
                double opFinisTime = Math.max(jobEndTime[op.job], machineEndTime[op.machine]) + op.duration;
                if (opFinisTime < bestFinishTime) {
                    earliestDoneOperation = op;
                    bestFinishTime = opFinisTime;
                }
            }
            // Find highest prio within machine of best
            int bestMachine = earliestDoneOperation.machine;
            Operation prioOp = findHighestPrioOperation(preferenceMatrix, schedulable, bestMachine, bestFinishTime, jobEndTime, machineEndTime);
            double prioOpFinishTime = Math.max(jobEndTime[prioOp.job], machineEndTime[prioOp.machine]) + prioOp.duration;
            schedulable.remove(prioOp);
            if (!jobQueues[prioOp.job].isEmpty()) schedulable.add(jobQueues[prioOp.job].remove(0));
            jobEndTime[prioOp.job] = prioOpFinishTime;
            machineEndTime[prioOp.machine] = prioOpFinishTime;
            operationStartTimes[prioOp.job][prioOp.jobOpIndex] = prioOpFinishTime - prioOp.duration;
        }
        double maxSpan = machineEndTime[0];
        for (int i = 1; i < machineEndTime.length; i++)
            if (machineEndTime[i] > maxSpan) maxSpan = machineEndTime[i];
        makespan = maxSpan;
    }

    // TODO - Optimise
    private Operation findHighestPrioOperation(int[][] preferenceMatrix, ArrayList<Operation> schedulable, int bestMachine,
                                               double bestFinishTime, double[] jobEndTime, double[] machineEndTime) {
        HashSet<Operation> schedulableForBestMachine = new HashSet<>(schedulable.size());

        for (Operation op : schedulable) {
            if (op.machine == bestMachine && Math.max(jobEndTime[op.job], machineEndTime[op.machine]) < bestFinishTime)
                schedulableForBestMachine.add(op);
        }

        for (int i = 0; i < JSP.numOfJobs; i++) {
            int preferenceJob = preferenceMatrix[bestMachine][i];
            for (Operation op : schedulableForBestMachine)
                if ( op.job == preferenceJob) return op;
        }
        return null;
    }

    private int[][] antPathToPreferenceMatrix(ArrayList<Integer> path) {
        int[][] preferenceMatrix = new int[JSP.numOfMachines][JSP.numOfJobs];
        int[] machineIndex = new int[JSP.numOfMachines];

        for (int i : path) {
            Operation op = JSP.getOperation(i);
            int machine = op.machine;
            preferenceMatrix[machine][machineIndex[machine]] = op.job;
            machineIndex[machine]++;
        }
        return preferenceMatrix;
    }

    public ArrayList<Integer> scheduleToAntPath() { // TODO This is slow af
        int[] jobOpIndexes = new int[JSP.numOfJobs];
        ArrayList<Integer> path = new ArrayList<>(JSP.numOfOperations);
        for (int i = 0; i < JSP.numOfOperations; i++) {
            int firstOperation = -1;
            int firstJob = -1;
            double firstStartTime = Double.POSITIVE_INFINITY;
            for (int j = 0; j < JSP.numOfJobs; j++) {
                if (jobOpIndexes[j] < JSP.numOfMachines){
                    double startTime = operationStartTimes[j][jobOpIndexes[j]];
                    if (startTime < firstStartTime){
                        firstStartTime = startTime;
                        firstJob = j;
                        firstOperation = j * JSP.numOfMachines + jobOpIndexes[j];
                    }
                }
            }
            path.add(firstOperation);
            jobOpIndexes[firstJob]++;
        }
//        System.out.println(path);
        return path;
    }


}
