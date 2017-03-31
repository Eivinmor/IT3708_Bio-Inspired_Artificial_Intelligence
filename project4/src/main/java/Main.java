import pso.Particle;
import representation.JSP;
import utility.DataReader;
import utility.Plotter;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class Main {

    public static void main(String[] args) {
        String[] strAr = DataReader.readOdtToStringArray(3);
        DataReader.makeRepresentation(strAr);

        for (int i = 0; i < JSP.numOfJobs; i++) {
            System.out.println(Arrays.toString(JSP.jobs[i]));
        }

        Particle p = new Particle();
        for (int i = 0; i < p.preferenceMatrix.length; i++) {
            System.out.println(Arrays.toString(p.preferenceMatrix[i]));
        }
        System.out.println();

        int[][] schedule = p.generateSchedule();
        for (int i = 0; i < schedule.length; i++) {
            System.out.println(Arrays.toString(schedule[i]));
        }

        System.out.println("\nOperation start times:");
        for (int i = 0; i < p.operationStartTimes.length; i++) {
            System.out.println(Arrays.toString(p.operationStartTimes[i]));
        }

        Plotter plotter = new Plotter();
        plotter.plotPSOSchedule(p);

        double minMakespan = Double.MAX_VALUE;
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Particle p2 = new Particle();
            p2.generateSchedule();
            plotter.plotPSOSchedule(p2);
            if (p2.makespan<minMakespan) {
                minMakespan = p2.makespan;
                System.out.println(minMakespan);
            }
        }
    }

}
