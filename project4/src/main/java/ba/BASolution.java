package ba;

import representation.JSP;
import representation.Operation;
import representation.Solution;

public class BASolution extends Solution implements Comparable<BASolution>{

    public BASolution(int[] foodSource) {
        this.makespan = 0;
        double[] jobEndTime = new double[JSP.numOfJobs];
        double[] machineEndTime = new double[JSP.numOfMachines];
        operationStartTimes = new double[JSP.numOfJobs][JSP.numOfMachines];
        int[] jobCount = new int[JSP.numOfJobs];

        for (int i = 0; i < foodSource.length; i++) {
            int job = foodSource[i];
            Operation op = JSP.getOperation(job * JSP.numOfMachines + jobCount[job]);
            jobCount[job]++;
            double maxStartTime = Math.max(jobEndTime[op.job], machineEndTime[op.machine]);
            operationStartTimes[op.job][op.jobOpIndex] = maxStartTime;
            jobEndTime[op.job] = maxStartTime + op.duration;
            machineEndTime[op.machine] = maxStartTime + op.duration;
            if (maxStartTime + op.duration > makespan) makespan = maxStartTime + op.duration;
        }
    }

    @Override
    public int compareTo(BASolution o) {
        if (this.makespan < o.makespan) return -1;
        if (this.makespan > o.makespan) return 1;
        return 0;

    }

}
