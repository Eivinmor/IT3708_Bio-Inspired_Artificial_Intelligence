package ba;

import representation.JSP;
import representation.Operation;
import representation.Solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class GTSolution extends Solution {

    GTSolution(int[] foodsource) {
        ArrayList<Operation> schedulable = new ArrayList<>(JSP.numOfMachines);
        double[] jobEndTime = new double[JSP.numOfJobs];
        double[] machineEndTime = new double[JSP.numOfMachines];
        operationStartTimes = new double[JSP.numOfJobs][JSP.numOfMachines];

        int[][] preferenceMatrix = foodSourceToPreferenceMatrix(foodsource);

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
                if (op.job == preferenceJob) return op;
        }
        return null;
    }

    private int[][] foodSourceToPreferenceMatrix(int[] foodSource) {
        int[][] preferenceMatrix = new int[JSP.numOfMachines][JSP.numOfJobs];
        int[] machineIndex = new int[JSP.numOfMachines];
        int[] jobCount = new int[JSP.numOfJobs];

        for (int job : foodSource) {
            Operation op = JSP.getOperation(job * JSP.numOfMachines + jobCount[job]);
            jobCount[job]++;
            int machine = op.machine;
            preferenceMatrix[machine][machineIndex[machine]] = op.job;
            machineIndex[machine]++;
        }
        return preferenceMatrix;
    }

    public int[] scheduleToFoodSource() { // TODO This is slow af
        int[] jobOpIndexes = new int[JSP.numOfJobs];
        int[] foodSource = new int[JSP.numOfOperations];
        for (int i = 0; i < JSP.numOfOperations; i++) {
            int firstJob = -1;
            double firstStartTime = Double.POSITIVE_INFINITY;
            for (int j = 0; j < JSP.numOfJobs; j++) {
                if (jobOpIndexes[j] < JSP.numOfMachines) {
                    double startTime = operationStartTimes[j][jobOpIndexes[j]];
                    if (startTime < firstStartTime) {
                        firstStartTime = startTime;
                        firstJob = j;
                    }
                }
            }
            foodSource[i] = firstJob;
            jobOpIndexes[firstJob]++;
        }
        return foodSource;
    }
}