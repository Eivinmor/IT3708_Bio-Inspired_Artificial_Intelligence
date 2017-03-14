package ga;

import representation.Edge;
import representation.Grid;
import utility.Tools;
import java.util.*;


public class Chromosome implements Comparable<Chromosome>{

    private int[] pixelGraph;
    public int[] pixelSegments;
    public int numOfSegments;
    private double cost;

    public Chromosome() {
        this.pixelGraph = new int[Grid.pixelArray.length];
        this.pixelSegments = new int[Grid.pixelArray.length];
        initaliseSegmentation();
        removeEdgesAboveThreshold();
        calculateSegmentation();
    }

    private void initaliseSegmentationRandom() {
        for (int i = 0; i < Grid.pixelArray.length; i++) {
            ArrayList<Integer> neighbours = new ArrayList<>(Grid.getNeighbourPixels(i));
            int randomIndex = Tools.random.nextInt(neighbours.size());
            pixelGraph[i] = neighbours.get(randomIndex);
        }
    }

    private void initaliseSegmentation(){
        // Initialising as MST through Prim's
        HashSet<Integer> visited = new HashSet<>(Grid.pixelArray.length);
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>();

        int current = 0;

        int i = 0;
        while (i < Grid.pixelArray.length){
            if (!visited.contains(current)){
                visited.add(current);
                for (int neighbour : Grid.getNeighbourPixels(current)) {
                    priorityQueue.add(new Edge(current, neighbour));
                }
                i++;
            }
            Edge edge = priorityQueue.poll();
            if (!visited.contains(edge.to)){
                pixelGraph[edge.from] = edge.to;
            }
            current = edge.to;
        }
    }

    // TODO Fix this
    private void removeEdgesAboveThreshold() {
        for (int i = 0; i < pixelGraph.length; i++) {
            if (Tools.rgbDistance(Grid.pixelArray[i], Grid.pixelArray[pixelGraph[i]]) >= Settings.initSegmentDistThreshold)
                pixelGraph[i] = i;
        }
    }

    public void calculateSegmentation() {
        // Set all pixels to unassigned
        for (int i = 0; i < pixelSegments.length; i++) pixelSegments[i] = -1;
        int curSegmentId = 0;
        ArrayList<Integer> curSegmentPixels;

        for (int i = 0; i < pixelGraph.length; i++) {
            curSegmentPixels = new ArrayList<>();
            if (pixelSegments[i] == -1) {
                curSegmentPixels.add(i);
                pixelSegments[i] = curSegmentId;
                int curPixelId = pixelGraph[i];

                // TODO Variation: Store all looped and set either curSegmentId or pixelSegments[curPixelId] for all instead of one at a time.
                while (pixelSegments[curPixelId] == -1) {
                    curSegmentPixels.add(curPixelId);
                    pixelSegments[curPixelId] = curSegmentId;
                    curPixelId = pixelGraph[curPixelId];
                }

                if (pixelSegments[curPixelId] != curSegmentId) {
                    for (int j = curSegmentPixels.size() - 1; j >= 0; j--) {
                        pixelSegments[curSegmentPixels.get(j)] = pixelSegments[curPixelId];
                    }
                }
                else curSegmentId++;
            }
        }
        numOfSegments = curSegmentId + 1;
    }

    private double calculateCost() {
        return calculateColorDistance();
    }

    private double calculateColorDistance() {
        double totalDistance = 0;
        // TODO Calculate all segment averages
        // TODO Compare each pixel to its segment average
        return totalDistance;
    }

    @Override
    public int compareTo(Chromosome o) {
        //TODO Burde bli kalkulert 1 gang per runde og så henter denne bare verdien
        if (this.cost > o.cost) return 1;
        else if (this.cost < o.cost) return -1;
        return 0;
    }

}

