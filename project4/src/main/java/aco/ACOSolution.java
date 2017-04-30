package aco;

import representation.JSP;
import representation.Operation;

import java.util.ArrayList;


public class ACOSolution extends representation.Solution{

    public ACOSolution(ArrayList<Integer> initPath) {
        this.makespan = 0;
        double[] jobEndTime = new double[JSP.numOfJobs];
        double[] machineEndTime = new double[JSP.numOfMachines];
        operationStartTimes = new double[JSP.numOfJobs][JSP.numOfMachines];

        ArrayList<Integer> path = new ArrayList<>(initPath);
        for (int i = 0; i < path.size(); i++) {
            Operation op = JSP.getOperation(path.get(i));
            double maxStartTime = Math.max(jobEndTime[op.job], machineEndTime[op.machine]);
            operationStartTimes[op.job][op.jobOpIndex] = maxStartTime;
            jobEndTime[op.job] = maxStartTime + op.duration;
            machineEndTime[op.machine] = maxStartTime + op.duration;
            if (maxStartTime + op.duration > makespan) makespan = maxStartTime + op.duration;
        }
    }
}
