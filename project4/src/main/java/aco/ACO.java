package aco;

import representation.JSP;
import utility.Tools;

import java.util.ArrayList;


public class ACO {

    private Edge[][] graph;

    public void runAlgorithm() {
        graph = generateGraph();
        ACOSolution totalBestSolution = new ACOSolution(new Ant());
        double totalBestMakespan = Double.POSITIVE_INFINITY;
        int startNode = -1;

        while (true) {
            double roundBestMakespan = Double.POSITIVE_INFINITY;
            ArrayList<Integer> bestPath = new ArrayList<>(JSP.numOfOperations);

            for (int i = 0; i < Settings.colonySize; i++) {
                Ant ant = new Ant();

                int[] curOpIndex = new int[JSP.numOfJobs];
                startNode = (startNode + 1) % JSP.numOfJobs;
                curOpIndex[startNode]++;
                ant.moveTo(startNode * JSP.numOfMachines);

                for (int j = 0; j < JSP.numOfOperations - 1; j++) {
                    ArrayList<Edge> possibleEdges = findPossibleEdges(ant, curOpIndex);
                    int nextNode = ant.chooseEdge(possibleEdges);
                    curOpIndex[JSP.getOperation(nextNode).job]++;
                    ant.moveTo(nextNode);
                }
                ACOSolution solution = new ACOSolution(ant);
                localPheromoneUpdate(ant.path);
                if (solution.makespan < roundBestMakespan) {
                    roundBestMakespan = solution.makespan;
//                    bestPath = new ArrayList<>(ant.path);
                    bestPath = ant.path;
                    if (solution.makespan < totalBestMakespan) {
                        totalBestSolution = solution;
                        totalBestMakespan = solution.makespan;
                        System.out.println(solution.makespan);
                        Tools.plotter.plotSolution(totalBestSolution);
                    }
                }
//                try {
//                    TimeUnit.MILLISECONDS.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
            Tools.plotter.plotSolution(totalBestSolution);
            globalPheromoneUpdate(bestPath, roundBestMakespan);
        }
    }

    private Edge[][] generateGraph(){
        Edge[][] graph = new Edge[JSP.numOfOperations][JSP.numOfOperations];
        for (int i = 0; i < JSP.numOfJobs; i++) {
            for (int j = 0; j < JSP.numOfMachines; j++) {
                int from = i * JSP.numOfMachines + j;

                for (int k = 0; k < JSP.numOfJobs; k++) {
                    if (k == i) {
                        int to = from + 1;
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

    private ArrayList<Edge> findPossibleEdges(Ant ant, int[] curOpIndex) {
        ArrayList<Edge> edges = new ArrayList<>(JSP.numOfJobs);
        for (int i = 0; i < JSP.numOfJobs; i++) {
            if (curOpIndex[i] < JSP.numOfMachines) {
                Edge edge = graph[ant.position][i * JSP.numOfMachines + curOpIndex[i]];
                if (edge != null) edges.add(edge);
            }
        }
        return edges;
    }

    private void localPheromoneUpdate(ArrayList<Integer> path) {
        for (int i = 0; i < JSP.numOfOperations - 1; i++) {
            Edge edge = graph[path.get(i)][path.get(i+1)];
            edge.pheromone = (1 - Settings.pheromoneDecay) * edge.pheromone
                    + Settings.pheromoneDecay * Settings.basePheromone;
        }
    }

    private void globalPheromoneUpdate(ArrayList<Integer> bestPath, double bestMakespan) {
        for (int i = 0; i < JSP.numOfOperations; i++) {
            for (Edge edge : graph[i]) {
                if (edge != null) edge.pheromone = (1 - Settings.pheromoneEvaporation) * edge.pheromone;
            }
        }
        for (int i = 0; i < JSP.numOfOperations - 1; i++) {
            Edge edge = graph[bestPath.get(i)][bestPath.get(i+1)];
            edge.pheromone += Settings.pheromoneEvaporation * (1 / bestMakespan);
        }
    }

    private void printGraph() {
        System.out.println();
        for (int i = 0; i < JSP.numOfOperations; i++) {
            for (int j = 0; j < JSP.numOfOperations; j++) {
                Edge edge = graph[i][j];
                if (edge == null) System.out.print(String.format("%4s ", "-"));
                else System.out.print(String.format("%4.2f ", 100*graph[i][j].pheromone));
            }
            System.out.println();
        }
        System.out.println();
    }

}
