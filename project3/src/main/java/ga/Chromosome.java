package ga;

import representation.Edge;
import representation.Grid;
import utility.Tools;

import java.awt.*;
import java.util.*;


public class Chromosome implements Comparable<Chromosome>{

    public int[] graph = new int[Grid.numOfPixels];
    public int[] segmentation = new int[Grid.numOfPixels];
    public int numOfSegments;
    public boolean segmentationIsOutdated = true;
    public double[] cost = new double[3];

    // New
    public Chromosome() {
        initaliseGraphAsMST();
        Tools.printDistance(this, false);
    }

    // Clone
    public Chromosome(Chromosome clonosome) {
        for (int i = 0; i < Grid.numOfPixels; i++) this.graph[i] = clonosome.graph[i];
    }

    // Crossover
    public Chromosome(Chromosome p1, Chromosome p2) {
        // TODO Crossover - divide chromosome into sections and for each sections choose from p1 or p2 randomly
        for (int i = 0; i < Grid.numOfPixels; i++) {
            if (Tools.random.nextBoolean()) graph[i] = p1.graph[i];
            else graph[i] = p2.graph[i];
        }
    }

    private void initaliseGraphAsRandom() {
        for (int i = 0; i < Grid.numOfPixels; i++) {
            ArrayList<Integer> neighbours = new ArrayList<>(Grid.getNeighbourPixels(i));
            int randomIndex = Tools.random.nextInt(neighbours.size());
            graph[i] = neighbours.get(randomIndex);
        }
    }

    private void initaliseGraphAsMST() {  //Using Prim's
        for (int i = 0; i < graph.length; i++) graph[i] = i;
        HashSet<Integer> visited = new HashSet<>(Grid.numOfPixels);
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>();

        int current = graph.length - 1; // Starts at the last pixel
        while (visited.size() < Grid.numOfPixels){
            if (!visited.contains(current)){
                visited.add(current);
                for (int neighbour : Grid.getNeighbourPixels(current)) {
                    priorityQueue.add(new Edge(current, neighbour));
                }
            }
            Edge edge = priorityQueue.poll();
            if (!visited.contains(edge.to)){
                graph[edge.to] = edge.from;
            }
            current = edge.to;
        }
    }

    private void removeEdgesAboveThreshold() {
        for (int i = 0; i < graph.length; i++) {
            if (Tools.colorDistance(Grid.pixelArray[i], Grid.pixelArray[graph[i]]) >= Settings.initSegmentDistThreshold)
                graph[i] = i;
        }
    }

    public void removeKLargestEdges(int k) {
        ArrayList<Edge> edges = calculateEdges();
        Collections.sort(edges);
        Collections.reverse(edges);
        for (int i = 0; i < k; i++) {
            Edge edge = edges.get(i);
            graph[edge.from] = edge.from;
        }
    }

    public void removeKRandomEdges(int k) {
        ArrayList<Edge> edges = calculateEdges();
        Collections.shuffle(edges);
        for (int i = 0; i < k; i++) {
            Edge edge = edges.get(i);
            graph[edge.from] = edge.from;
        }
    }

    public ArrayList<Edge> calculateEdges() {
        ArrayList<Edge> edges = new ArrayList<>(graph.length);
        for (int i = 0; i < graph.length; i++) {
            edges.add(new Edge(i, graph[i])); // TODO Kan utelukke de som er til seg selv om ønskelig
        }
        return edges;
    }

    public void calculateSegmentation() {
        // Set all pixels to unassigned
        for (int i = 0; i < segmentation.length; i++) segmentation[i] = -1;
        int curSegmentId = 0;
        ArrayList<Integer> curPath;

        for (int rootPixel = 0; rootPixel < Grid.numOfPixels; rootPixel++) {

            curPath = new ArrayList<>();

            if (segmentation[rootPixel] == -1) {
                curPath.add(rootPixel);
                segmentation[rootPixel] = curSegmentId;
                int curPixel = graph[rootPixel];

                // TODO Variation: Store all looped and set either curSegmentId or segmentation[curPixel] for all instead of one at a time.
                while (segmentation[curPixel] == -1) {
                    curPath.add(curPixel);
                    segmentation[curPixel] = curSegmentId;
                    curPixel = graph[curPixel];
                }
                if (segmentation[curPixel] != curSegmentId) {
                    for (int segmentPixel : curPath) {
                        segmentation[segmentPixel] = segmentation[curPixel];
                    }
                }
                else curSegmentId++;
            }
        }
        numOfSegments = curSegmentId;
        segmentationIsOutdated = false;
    }

    public void mutate() {
        // TODO Logarithmic mutation prob if necessary
        double r = Tools.random.nextDouble();
        if (r < Settings.mutateAddEdgeRate) mutateAddEdge();
        else if ((r += Settings.mutateAddEdgeRate) < Settings.mutateSetRandomEdgeRate) mutateSetRandomEdge();
        else if ((r + Settings.mutateSetRandomEdgeRate) < Settings.mutateRemoveEdge) mutateRemoveEdge();
        segmentationIsOutdated = true;
    }

    private void mutateSetRandomEdge() {
        int i = Tools.random.nextInt(Grid.numOfPixels);
        ArrayList<Integer> neighbours = Grid.getNeighbourPixels(i);
        graph[i] = neighbours.get(Tools.random.nextInt(neighbours.size()));
    }

    private void mutateAddEdge() {
        ArrayList<Integer> noEdge = new ArrayList<>();
        for (int i = 0; i < Grid.numOfPixels; i++) {
            if (graph[i] == i) noEdge.add(i);
        }
        if (noEdge.size() > 0) {
            int i = Tools.random.nextInt(noEdge.size());
            ArrayList<Integer> neighbours = Grid.getNeighbourPixels(noEdge.get(i));
            graph[noEdge.get(i)] = neighbours.get(Tools.random.nextInt(neighbours.size()));
        }
    }

    private void mutateRemoveEdge() {
        int i = Tools.random.nextInt(Grid.numOfPixels);
        graph[i] = i;
    }

    public double overallColorDeviation() {
        if (segmentationIsOutdated) calculateSegmentation();

        // Calculate average segment color
        float[][] segmentAvgColor = new float[numOfSegments][3];
        int[] segmentSize = new int[numOfSegments];
        for (int i = 0; i < Grid.numOfPixels; i++) {
            float[] colorValues = Grid.pixelArray[i].getRGBColorComponents(null);
            int segment = segmentation[i];
            for (int j = 0; j < colorValues.length; j++) segmentAvgColor[segment][j] = colorValues[j];
            segmentSize[segment]++;
        }
        for (int i = 0; i < segmentAvgColor.length; i++) {
            for (int j = 0; j < 3; j++) segmentAvgColor[i][j] = segmentAvgColor[i][j] / segmentSize[i];
        }
        // Compare pixel color to avg
        double overallDeviation = 0;
        for (int i = 0; i < Grid.numOfPixels; i++) {
            float[] segmentColorValues = segmentAvgColor[segmentation[i]];
            overallDeviation += Tools.colorDistance(Grid.pixelArray[i],
                    new Color(segmentColorValues[0], segmentColorValues[1], segmentColorValues[2]));
        }
        return overallDeviation;
    }

    public double edgeValue () {
        if (segmentationIsOutdated) calculateSegmentation();

        double totalEdgeValue = 0;
        for (int i = 0; i < Grid.numOfPixels; i++) {
            ArrayList<Integer> neighbours = Grid.getNeighbourPixels(i);
            for (int nb : neighbours) {
                if (segmentation[i] != segmentation[nb])
                    totalEdgeValue -= Tools.colorDistance(Grid.pixelArray[i], Grid.pixelArray[nb]);
            }
        }
        return totalEdgeValue;
    }

    public double connectivity() {
        if (segmentationIsOutdated) calculateSegmentation();

        double connectivity = 0;
        for (int i = 0; i < Grid.numOfPixels; i++) {
            ArrayList<Integer> neighbours = Grid.getNeighbourPixels(i);
            int connectedNeighbours = 0;
            for (int j : neighbours) {
                if (segmentation[i] != segmentation[j]) {
                    connectedNeighbours++;
                    connectivity += 1.0 / connectedNeighbours;
                }
            }
        }
        return connectivity;
    }

    public void calculateCost() { // TODO Manually call this before comparing
        if (Settings.useOverallDeviation) cost[0] = overallColorDeviation();
        if (Settings.useEdgeValue) cost[1] = edgeValue();
        if (Settings.useConnectivity) cost[2] = connectivity();
    }

    // Pareto
    public boolean dominates(Chromosome o) {
        if (cost[0] <= o.cost[0] && cost[1] <= o.cost[1] && cost[2] <= o.cost[2]) {
            if (cost[0] < o.cost[0] || cost[1] < o.cost[1] || cost[2] < o.cost[2]) {
                return true;
            }
        }
        return false;
    }

    public void printCost() {
        System.out.println(cost[0] + " " + cost[1] + " " + cost[2]);
    }
}

