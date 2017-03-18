package ga;

import representation.Edge;
import representation.Grid;
import utility.Tools;
import java.util.*;


// TODO Mutation - edge to random neighbour
// TODO Mutation - remove edge (change to self)

// TODO Crossover - for each gene choose from p1 or p2 randomly
// TODO Crossover - divide chromosome into sections and for each sections choose from p1 or p2 randomly


public class Chromosome {

    public int[] pixelGraph;
    public int[] pixelSegments;
    public int numOfSegments;

    public Chromosome() {
        this.pixelGraph = new int[Grid.pixelArray.length];
        this.pixelSegments = new int[Grid.pixelArray.length];

        initaliseSegmentationAsMST();
//        Tools.printDistance(this, false);
        removeKLargestEdges(15000); // TODO Gjøre om til å ta inn prosent
        calculateSegmentation();
    }

    private void initaliseSegmentationRandom() {
        for (int i = 0; i < Grid.pixelArray.length; i++) {
            ArrayList<Integer> neighbours = new ArrayList<>(Grid.getNeighbourPixels(i));
            int randomIndex = Tools.random.nextInt(neighbours.size());
            pixelGraph[i] = neighbours.get(randomIndex);
        }
    }

    private void initaliseSegmentationAsMST() {  //Using Prim's
        for (int i = 0; i < pixelGraph.length; i++) pixelGraph[i] = i;
        HashSet<Integer> visited = new HashSet<>(Grid.pixelArray.length);
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>();

        int current = pixelGraph.length - 1; // Starts at the last pixel
        while (visited.size() < Grid.pixelArray.length){
            if (!visited.contains(current)){
                visited.add(current);
                for (int neighbour : Grid.getNeighbourPixels(current)) {
                    priorityQueue.add(new Edge(current, neighbour));
                }
            }
            Edge edge = priorityQueue.poll();
            if (!visited.contains(edge.to)){
                pixelGraph[edge.to] = edge.from;
            }
            current = edge.to;
        }
    }

    private void removeEdgesAboveThreshold() {
        for (int i = 0; i < pixelGraph.length; i++) {
            if (Tools.colorDistance(Grid.pixelArray[i], Grid.pixelArray[pixelGraph[i]]) >= Settings.initSegmentDistThreshold)
                pixelGraph[i] = i;
        }
    }

    private void removeKLargestEdges(int k) {
        ArrayList<Edge> edges = calculateEdges();
        Collections.sort(edges);
        Collections.reverse(edges);
        for (int i = 0; i < k; i++) {
            Edge edge = edges.get(i);
            pixelGraph[edge.from] = edge.from;
        }
    }


    public ArrayList<Edge> calculateEdges() {
        ArrayList<Edge> edges = new ArrayList<>(pixelGraph.length);
        for (int i = 0; i < pixelGraph.length; i++) {
            edges.add(new Edge(i, pixelGraph[i])); // TODO Kan utelukke de som er til seg selv om ønskelig
        }
        return edges;
    }

    public void calculateSegmentation() {
        // Set all pixels to unassigned
        for (int i = 0; i < pixelSegments.length; i++) pixelSegments[i] = -1;
        int curSegmentId = 0;
        ArrayList<Integer> curPath;

        for (int rootPixel = 0; rootPixel < Grid.pixelArray.length; rootPixel++) {

            curPath = new ArrayList<>();

            if (pixelSegments[rootPixel] == -1) {
                curPath.add(rootPixel);
                pixelSegments[rootPixel] = curSegmentId;
                int curPixel = pixelGraph[rootPixel];

                // TODO Variation: Store all looped and set either curSegmentId or pixelSegments[curPixel] for all instead of one at a time.
                while (pixelSegments[curPixel] == -1) {
                    curPath.add(curPixel);
                    pixelSegments[curPixel] = curSegmentId;
                    curPixel = pixelGraph[curPixel];
                }
                if (pixelSegments[curPixel] != curSegmentId) {
                    for (int segmentPixel : curPath) {
                        pixelSegments[segmentPixel] = pixelSegments[curPixel];
                    }
                }
                else curSegmentId++;
            }
        }
        numOfSegments = curSegmentId;
    }

}

