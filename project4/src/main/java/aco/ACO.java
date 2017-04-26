package aco;

import representation.JSP;

import java.util.ArrayList;

public class ACO {

    ArrayList<Edge>[] graph;

    public void runAlgorithm() {
        graph = generateGraph();


        // TODO Representation of disjunctive graph

        // For each ant

            // Assign to start node (first in one of the jobs)
            // Init ant path array (add first node)

            // For each operation - 1
                // Choose next move
                // Move
                // Store path
            // Update local pheromone on used edge (reduction)

        // Lay pheromone on best global path

    }

    private ArrayList<Edge>[] generateGraph(){
        ArrayList<Edge>[] graph = new ArrayList[JSP.numOfJobs * JSP.numOfMachines];
        for (int i = 0; i < JSP.numOfJobs; i++) {
            for (int j = 0; j < JSP.numOfMachines; j++) {
                int from = i * JSP.numOfMachines + j;
                graph[from] = new ArrayList<>();

                for (int k = 0; k < JSP.numOfJobs; k++) {
                    if (k == i) {
                        int to = k * JSP.numOfMachines + j + 1;
                        if (j < JSP.numOfMachines - 1)graph[from].add(new Edge(from, to));
                        continue;
                    }
                    for (int l = 0; l < JSP.numOfMachines; l++) {
                        int to = k * JSP.numOfMachines + l;
                        graph[from].add(new Edge(from, to));
                    }
                }
            }
        }
        return graph;
    }

}
