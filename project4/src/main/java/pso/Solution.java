package pso;

import representation.JSP;
import representation.Operation;

import java.util.ArrayList;
import java.util.Arrays;


public class Solution {

    final int[][] schedule;
    final double makespan;
    public double[][] operationStartTimes;

    Solution(Particle particle) {
        schedule = new int[JSP.numOfMachines][JSP.numOfJobs];
        ArrayList<Operation> schedulable = new ArrayList<>(JSP.numOfMachines);
        double[] jobEndTime = new double[JSP.numOfJobs];
        double[] machineEndTime = new double[JSP.numOfMachines];
        int[] machineOperations = new int[JSP.numOfMachines];
        operationStartTimes = new double[JSP.numOfJobs][JSP.numOfMachines];

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
            Operation prioOp = earliestDoneOperation;
            for (Operation op : schedulable) {
                if (op.machine == bestMachine
                        && particle.preferenceMatrix[op.machine][op.job] < particle.preferenceMatrix[prioOp.machine][prioOp.job])
                    prioOp = op;
            }
            double prioOpFinishTime = Math.max(jobEndTime[prioOp.job], machineEndTime[prioOp.machine]) + prioOp.duration;
            schedulable.remove(prioOp);
            if (!jobQueues[prioOp.job].isEmpty()) schedulable.add(jobQueues[prioOp.job].remove(0));
            schedule[prioOp.machine][prioOp.job] = machineOperations[prioOp.machine];
            machineOperations[prioOp.machine]++;
            jobEndTime[prioOp.job] = prioOpFinishTime;
            machineEndTime[prioOp.machine] = prioOpFinishTime;
            operationStartTimes[prioOp.job][prioOp.jobOpIndex] = prioOpFinishTime - prioOp.duration;
        }
        double maxSpan = machineEndTime[0];
        for (int i = 1; i < machineEndTime.length; i++)
            if (machineEndTime[i] > maxSpan) maxSpan = machineEndTime[i];
        makespan = maxSpan;
    }

}
