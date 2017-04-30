package aco;

import representation.JSP;
import representation.Operation;

import java.util.ArrayList;


public class ACOSolution extends representation.Solution{

    public ACOSolution(ArrayList<Integer> initOpPath) {
        this.makespan = 0;
        double[] jobEndTime = new double[JSP.numOfJobs];
        double[] machineEndTime = new double[JSP.numOfMachines];
        operationStartTimes = new double[JSP.numOfJobs][JSP.numOfMachines];

        ArrayList<Integer> path = new ArrayList<>(initOpPath);
        for (int i = 0; i < path.size(); i++) {
            Operation op = JSP.getOperation(path.get(i));
            double maxStartTime = Math.max(jobEndTime[op.job], machineEndTime[op.machine]);
            operationStartTimes[op.job][op.jobOpIndex] = maxStartTime;
            jobEndTime[op.job] = maxStartTime + op.duration;
            machineEndTime[op.machine] = maxStartTime + op.duration;
            if (maxStartTime + op.duration > makespan) makespan = maxStartTime + op.duration;
        }
    }

    public ACOSolution(int[] initJobPath) {
        this.makespan = 0;
        double[] jobEndTime = new double[JSP.numOfJobs];
        double[] machineEndTime = new double[JSP.numOfMachines];
        operationStartTimes = new double[JSP.numOfJobs][JSP.numOfMachines];
        int[] jobCount = new int[JSP.numOfJobs];

        for (int i = 0; i < initJobPath.length; i++) {
            int job = initJobPath[i];
            Operation op = JSP.jobs[job][jobCount[job]];
            jobCount[job]++;
            double maxStartTime = Math.max(jobEndTime[op.job], machineEndTime[op.machine]);
            operationStartTimes[op.job][op.jobOpIndex] = maxStartTime;
            jobEndTime[op.job] = maxStartTime + op.duration;
            machineEndTime[op.machine] = maxStartTime + op.duration;
            if (maxStartTime + op.duration > makespan) makespan = maxStartTime + op.duration;
        }
    }
}
