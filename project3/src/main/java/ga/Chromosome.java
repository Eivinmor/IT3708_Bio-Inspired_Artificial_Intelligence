package ga;

import representation.Grid;
import java.util.*;


public class Chromosome implements Comparable<Chromosome>{

    private int[] pixelGraph;
    public int[] pixelSegments;
    private double cost;
    public int numOfSegments;

    public Chromosome() {
        this.pixelGraph = new int[Grid.pixelArray.length];
        initaliseSegmentation();
        this.pixelSegments = new int[Grid.pixelArray.length];
        calculateSegmentation();
    }

    private void initaliseSegmentation(){
        // Initialising as MST through Kruskal's
        ArrayList<HashSet<Integer>> pixelSegments = new ArrayList<>();
        int[] pixelSegmentIds = new int[Grid.pixelArray.length];
        for (int i = 0; i < pixelSegmentIds.length; i++) pixelSegmentIds[i] = -1;

        for (int i = 0; i < pixelGraph.length; i++) pixelGraph[i] = -1;
        ArrayList<Integer[]> orderedEdges = new ArrayList<>();
        for (ArrayList<Integer[]> edges : Grid.pixelNeighbourDistances.values()) {
            orderedEdges.addAll(edges);
        }

        for (Integer[] edge : orderedEdges) {
            // TODO Assign edges based on shortest
        }
        // Assign self-edge those without outgoing connections
        for (int i = 0; i < pixelGraph.length; i++) {
            if (pixelGraph[i] == -1) {
                pixelGraph[i] = i;
            }
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
        HashSet<Integer> visitedPixels = new HashSet<>(pixelGraph.length);
        double totalColorDistance = 0;
        for (int i = 0; i < pixelGraph.length; i++) {
            if (!visitedPixels.contains(i)) {
                visitedPixels.add(i);
                int nextPixelId = i;
                while (nextPixelId != -1) {
                    // TODO Gjøre ferdig
                }
            }
        }
        return totalColorDistance;
    }

    @Override
    public int compareTo(Chromosome o) {
        //TODO Burde bli kalkulert 1 gang per runde og så henter denne bare verdien
        if (this.calculateCost() > o.calculateCost()) return 1;
        else if (this.calculateCost() < o.calculateCost()) return -1;
        return 0;
    }
}

