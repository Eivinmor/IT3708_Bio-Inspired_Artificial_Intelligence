package aco;

import representation.JSP;
import utility.Tools;

import java.util.ArrayList;
import java.util.Arrays;


public class ACO {

    private Edge[][] graph;

    public void runAlgorithm() {
        graph = generateGraph();
        int startNode = -1;
        double bestMakespan = Double.POSITIVE_INFINITY;
        ArrayList<Integer> bestPath = new ArrayList<>(JSP.numOfOperations);

        Solution totalBestSolution = null;
        double totalBestMakespan = Double.POSITIVE_INFINITY;

        while (true) {
            for (int i = 0; i < Settings.colonySize; i++) {
                Ant ant = new Ant();
                double[] jobEndTime = new double[JSP.numOfJobs];
                double[] machineEndTime = new double[JSP.numOfMachines];
                int[] curOpIndex = new int[JSP.numOfJobs];

                startNode = (startNode + 1) % JSP.numOfJobs;
                curOpIndex[JSP.getOperation(startNode * JSP.numOfJobs).job]++;
                ant.moveTo(startNode * JSP.numOfJobs);

                for (int j = 0; j < JSP.numOfOperations - 1; j++) {
                    ArrayList<Edge> possibleEdges = findPossibleEdges(ant, curOpIndex);
                    int nextNode = ant.chooseEdge(possibleEdges, jobEndTime, machineEndTime);
                    curOpIndex[JSP.getOperation(nextNode).job]++;
                    ant.moveTo(nextNode);
                }

                localPheromoneUpdate(ant);
                Solution solution = new Solution(ant);
                if (solution.makespan < bestMakespan) {
                    bestMakespan = solution.makespan;
                    bestPath = new ArrayList<>(ant.path);
                    if (solution.makespan < totalBestMakespan)
                        totalBestSolution = solution;
                }
            }
            globalPheromoneUpdate(bestPath, bestMakespan);
            Tools.plotter.plotACOSolution(totalBestSolution);
        }
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

    private ArrayList<Edge> findPossibleEdges(Ant ant, int[] curOpIndex) {
        ArrayList<Edge> edges = new ArrayList<>(JSP.numOfJobs);
        for (int i = 0; i < JSP.numOfJobs; i++) {
            if (curOpIndex[i] < 6) {
                Edge edge = graph[ant.position][i * JSP.numOfMachines + curOpIndex[i]];
                if (edge != null) edges.add(edge);
            }
        }
        return edges;
    }

    private void localPheromoneUpdate(Ant ant) {
//        System.out.println(ant.path);
        for (int i = 0; i < JSP.numOfOperations - 1; i++) {
            Edge edge = graph[ant.path.get(i)][ant.path.get(i+1)];
//            System.out.println(edge);
            edge.pheromone = (1 - Settings.pheromoneDecay) * edge.pheromone
                    + Settings.pheromoneDecay * Settings.basePheromone;
        }
    }

    private void globalPheromoneUpdate(ArrayList<Integer> bestPath, double bestMakespan) {
        for (int i = 0; i < JSP.numOfOperations; i++) {
            for (Edge edge : graph[i]) {
                if (edge != null) edge.pheromone = (1 - Settings.pheromoneDecay) * edge.pheromone;
            }
        }
        for (int i = 0; i < JSP.numOfOperations - 1; i++) {
            Edge edge = graph[bestPath.get(i)][bestPath.get(i+1)];
            edge.pheromone += Settings.pheromoneDecay * (1/bestMakespan);
        }

    }

}
