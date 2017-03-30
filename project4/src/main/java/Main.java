import pso.Particle;
import representation.JSP;
import representation.Operation;
import utility.DataReader;

import java.util.Arrays;


public class Main {

    public static void main(String[] args) {
        String[] strAr = DataReader.readOdtToStringArray(0);
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

    }

}
