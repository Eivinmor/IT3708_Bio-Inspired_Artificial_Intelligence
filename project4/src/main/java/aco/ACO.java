package aco;

import representation.JSP;
import representation.Operation;

import java.util.ArrayList;

public class ACO {

    private Edge[][] graph;
    private Ant[] colony;

    public void runAlgorithm() {
        graph = generateGraph();
        colony = generateColony();
        int startNode = -1;

        for (Ant ant : colony) {
            double[] jobEndTime = new double[JSP.numOfJobs];
            double[] machineEndTime = new double[JSP.numOfMachines];
            int[] curOpIndex = new int[JSP.numOfJobs];

            startNode = (startNode + 1) % JSP.numOfJobs;
            ant.moveTo(startNode * JSP.numOfJobs);

            for (int i = 0; i < JSP.numOfOperations - 1; i++) {

                // Find possible edges
                // Make ant decide and move (store path in ant)
            }

            // Update local pheromone on path (reduction)
        }
        // Lay pheromone on best global path
    }

    private Edge[][] generateGraph(){
        Edge[][] graph = new Edge[JSP.numOfOperations][JSP.numOfOperations];
        for (int i = 0; i < JSP.numOfJobs; i++) {
            for (int j = 0; j < JSP.numOfMachines; j++) {
                int from = i * JSP.numOfMachines + j;

                for (int k = 0; k < JSP.numOfJobs; k++) {
                    if (k == i) {
                        int to = k * JSP.numOfMachines + j + 1;
                        if (j < JSP.numOfMachines - 1) graph[from][to] = new Edge(from, to);
                        continue;
                    }
                    for (int l = 0; l < JSP.numOfMachines; l++) {
                        int to = k * JSP.numOfMachines + l;
                        graph[from][to] = new Edge(from, to);
                    }
                }
            }
        }
        return graph;
    }

    private Ant[] generateColony() {
        Ant[] colony = new Ant[Settings.colonySize];
        for (int i = 0; i < Settings.colonySize; i++) colony[i] = new Ant();
        return colony;
    }

    private Operation getOperation(int graphIndex) {
        return JSP.jobs[graphIndex / JSP.numOfMachines][graphIndex % JSP.numOfMachines];
    }

    private ArrayList<Edge> findPossibleEdges(Ant ant) {
        ArrayList<Edge> edges = new ArrayList<>(JSP.numOfJobs);
        for (int i = 0; i < JSP.numOfOperations; i++) {
            if (!ant.visited.contains(i)) {
                Edge edge = graph[ant.position][i];
                if (edge != null) edges.add(edge);
            }
        }
        return edges;
    }

}
