package ga;

import java.util.ArrayList;
import java.util.HashSet;


public class Chromosome implements Comparable<Chromosome>{

    public int[] pixelGraph;
    private int[] pixelSegments;
    private double cost;

    public Chromosome() {
        this.pixelSegments = new int[pixelGraph.length];
    }
    
    public void calculateSegmentation() {
        // Set all pixels to unassigned
        for (int i = 0; i < pixelSegments.length; i++) {
            pixelSegments[i] = -1;
        }
        int curSegmentId = 0;
        int counter;
        ArrayList<Integer> curSegmentPixels = new ArrayList<>();
        for (int i = 0; i < pixelGraph.length; i++) {
            counter = 0;
            if (pixelSegments[i] == -1) {
                curSegmentPixels.add(i);
                pixelSegments[i] = curSegmentId;
                int curPixelId = pixelGraph[i];
                counter++;

                // TODO Variation: Store all looped and set either curSegmentId or pixelSegments[curPixelId] for all instead of one at a time.
                while (pixelSegments[curPixelId] == -1) {
                    curSegmentPixels.add(curPixelId);
                    pixelSegments[curPixelId] = curSegmentId;
                    curPixelId = pixelGraph[curPixelId];
                    counter++;
                }

                if (pixelSegments[curPixelId] != curSegmentId) {
                    counter--;
                    while (counter >= 0) {
                        pixelSegments[curSegmentPixels.get(counter)] = pixelSegments[curPixelId];
                        counter--;
                    }
                }
                else curSegmentId++;
            }
        }
        // TODO - number of clusters = curClusterId + 1;
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

