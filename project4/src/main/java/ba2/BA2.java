package ba2;


import representation.JSP;

import java.util.ArrayList;
import java.util.Collections;

public class BA2 {

    public void runAlgorithm() {



        // Initiate scouted solutions (generate random solutions)
        BA2Solution bestSolution;
        ArrayList<BA2Solution> solutions = scatterScouts(Settings.numOfScoutBees);

        // START LOOP

        // Sort scouted sites (generate random solutions)
        Collections.sort(solutions);
        bestSolution = solutions.get(0);

        // Present nb best of the scouted sites
        ArrayList<BA2Solution> bestSolutions = new ArrayList<>(solutions.subList(
                0, Settings.numOfBestSites));

        for (int i = 0; i < Settings.numOfEliteSites; i++) {
            // Elite search
            bestSolutions.set(i, searchNeighbourhood(bestSolutions.get(i), Settings.beesPerEliteSite));
        }
        for (int i = Settings.numOfEliteSites; i < Settings.numOfBestSites; i++) {
            // Best search
            bestSolutions.set(i, searchNeighbourhood(bestSolutions.get(i), Settings.beesPerBestSite));
        }

        // Perform local search on patches (scatter within neighbourhood)

        // Shrink neighbourhoods

        // Site abandonment

        // Scout new sites

    }

    private ArrayList<BA2Solution> scatterScouts(int numberOfScouts) {
        ArrayList<Integer> jobs = new ArrayList<>(JSP.numOfOperations);
        for (int i = 0; i < JSP.numOfJobs; i++) {
            for (int j = 0; j < JSP.numOfMachines; j++) jobs.add(i);
        }
        ArrayList<BA2Solution> solutions = new ArrayList<>(numberOfScouts);
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

    private BA2Solution searchNeighbourhood(BA2Solution solution, int numOfBees) {
        BA2Solution bestNeighbourhoodSolution = null;
        for (int i = 0; i < numOfBees; i++) {
               
        }
        return bestNeighbourhoodSolution;
    }

}
