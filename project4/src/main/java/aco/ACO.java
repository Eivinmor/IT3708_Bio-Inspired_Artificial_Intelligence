package aco;

import representation.JSP;
import utility.Tools;

import java.util.ArrayList;


public class ACO {

    private Edge[][] graph;

    public void runAlgorithm() {
        graph = generateGraph();
        ArrayList<Integer> totalBestPath = new ArrayList<>(JSP.numOfOperations);;
        ACOSolution totalBestSolution = new ACOSolution(new ArrayList<>());
        totalBestSolution.makespan = Double.POSITIVE_INFINITY;
        int startNode = -1;

        while (true) {
            ArrayList<Integer> roundBestPath = new ArrayList<>(JSP.numOfOperations);
            ACOSolution roundBestSolution = new ACOSolution(new ArrayList<>());
            roundBestSolution.makespan = Double.POSITIVE_INFINITY;

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
                ACOSolution solution = new ACOSolution(ant.path);
                localPheromoneUpdate(ant.path);

                if (solution.makespan < roundBestSolution.makespan) {
                    roundBestPath = new ArrayList<>(ant.path);
                    roundBestSolution = solution;
                }
            }
            if (roundBestSolution.makespan < totalBestSolution.makespan) {
                totalBestPath = new ArrayList<>(roundBestPath);
                totalBestSolution = roundBestSolution;
            }
            Tools.plotter.plotSolution(totalBestSolution);
            totalBestPath = variableNeighbourSearch(totalBestPath, totalBestPath);
            totalBestSolution = new ACOSolution(totalBestPath);
            globalPheromoneUpdate(roundBestPath, roundBestSolution.makespan);
            System.out.println(totalBestSolution.makespan);
//            printGraph();
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

    private ArrayList<Integer> variableNeighbourSearch(ArrayList<Integer> path, ArrayList<Integer> totalBestPath) {
        ACOSolution initialSolution = new ACOSolution(pathToFoodSource(totalBestPath));
        int[] firstFoodSource = pathToFoodSource(path);
        int step = 0;
        int p = 1;

        int alpha = Tools.random.nextInt(JSP.numOfOperations);
        int beta = Tools.random.nextInt(JSP.numOfOperations);
        while (beta == alpha) beta = Tools.random.nextInt(JSP.numOfOperations);
        firstFoodSource = exchangingProcess(firstFoodSource, alpha, beta);

        alpha = Tools.random.nextInt(JSP.numOfOperations);
        beta = Tools.random.nextInt(JSP.numOfOperations);
        while (beta == alpha) beta = Tools.random.nextInt(JSP.numOfOperations);
        firstFoodSource = insertingProcess(firstFoodSource, alpha, beta);

        alpha = Tools.random.nextInt(JSP.numOfOperations);
        beta = Tools.random.nextInt(JSP.numOfOperations);
        while (beta == alpha) beta = Tools.random.nextInt(JSP.numOfOperations);
        firstFoodSource = exchangingProcess(firstFoodSource, alpha, beta);

        while (step <= JSP.numOfOperations * (JSP.numOfOperations - 1)){
            int[] secondFoodSource;
            alpha = Tools.random.nextInt(JSP.numOfOperations);
            beta = Tools.random.nextInt(JSP.numOfOperations);
            while (beta == alpha) beta = Tools.random.nextInt(JSP.numOfOperations);
            if (p == 1) {
                secondFoodSource = exchangingProcess(firstFoodSource, alpha, beta);
            }
            else {
                secondFoodSource = insertingProcess(firstFoodSource, alpha, beta);
            }
            if (new ACOSolution(secondFoodSource).makespan < new ACOSolution(firstFoodSource).makespan) {
                firstFoodSource = secondFoodSource.clone();
            }
            else {
                p = Math.abs(p - 1);
            }
            step++;
        }
        if (new ACOSolution(firstFoodSource).makespan < initialSolution.makespan) {
            return foodSourceToPath(firstFoodSource);
        }
        return path;
    }

    private int[] exchangingProcess(int[] foodSource, int alpha, int beta) {
        int[] newFoodSource = foodSource.clone();
        newFoodSource[alpha] = foodSource[beta];
        newFoodSource[beta] = foodSource[alpha];
        return newFoodSource;
    }

    private int[] insertingProcess(int[] foodSource, int alpha, int beta) {
        int[] newFoodSource = foodSource.clone();
        if (alpha > beta) {
            for (int i = beta; i < alpha; i++) {
                newFoodSource[i+1] = foodSource[i];
            }
        }
        else {
            for (int i = alpha; i < foodSource.length - 1; i++) {
                if (i == beta) break;
                newFoodSource[i] = foodSource[i + 1];
            }
        }
        newFoodSource[beta] = foodSource[alpha];
        return newFoodSource;
    }

    private int[] pathToFoodSource(ArrayList<Integer> path) {
        int[] foodSource = new int[path.size()];
        for (int i = 0; i < path.size(); i++) {
            foodSource[i] = path.get(i) / JSP.numOfMachines;
        }
        return foodSource;
    }

    private ArrayList<Integer> foodSourceToPath(int[] foodSource) {
        int[] jobOpIndex = new int[JSP.numOfJobs];
        ArrayList<Integer> path = new ArrayList<>(foodSource.length);
        for (int i = 0; i < foodSource.length; i++) {
            path.add(foodSource[i] * JSP.numOfMachines + jobOpIndex[foodSource[i]]);
            jobOpIndex[foodSource[i]]++;
        }
        return path;
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
