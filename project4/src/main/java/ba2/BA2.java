package ba2;


import representation.JSP;

import java.util.ArrayList;
import java.util.Collections;

public class BA2 {

    public void runAlgorithm() {


        // Scout sites (generate random solutions)
        ArrayList<BA2Solution> scoutedSites = scatterScouts();

        // Sort scouted sites
        Collections.sort(scoutedSites);

        // Present nb best of the scouted sites
        ArrayList<BA2Solution> bestSolutions =
                new ArrayList<>(scoutedSites.subList(0, Settings.numOfEliteSites));

        // 

    }

    private ArrayList<BA2Solution> scatterScouts() {
        ArrayList<Integer> jobs = new ArrayList<>(JSP.numOfOperations);
        for (int i = 0; i < JSP.numOfJobs; i++) {
            for (int j = 0; j < JSP.numOfMachines; j++) jobs.add(i);
        }
        ArrayList<BA2Solution> solutions = new ArrayList<>(Settings.numOfScoutBees);
        for (int i = 0; i < Settings.numOfScoutBees; i++) {
            int[] foodSource = new int[JSP.numOfOperations];
            Collections.shuffle(jobs);
            for (int j = 0; j < JSP.numOfOperations; j++) {
                foodSource[j] = jobs.get(j);
            }
            solutions.add(new BA2Solution(foodSource));
        }
        return solutions;
    }

}
