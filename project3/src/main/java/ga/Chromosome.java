package ga;

import representation.Edge;
import representation.Grid;
import utility.Tools;
import java.util.*;


// TODO Mutation - edge to random neighbour
// TODO Mutation - remove edge (change to self)

// TODO Crossover - for each gene choose from p1 or p2 randomly
// TODO Crossover - divide chromosome into sections and for each sections choose from p1 or p2 randomly


public class Chromosome implements Comparable<Chromosome>{

    private int[] pixelGraph;
    public int[] pixelSegments;
    public int numOfSegments;
    private double cost;

    public Chromosome() {
        this.pixelGraph = new int[Grid.pixelArray.length];
        this.pixelSegments = new int[Grid.pixelArray.length];

//        initaliseSegmentationRandom();
        initaliseSegmentationAsMST();
//        printDistance(false);
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

//    private void initaliseSegmentationAsMST(){
//        for (int i = 0; i < pixelGraph.length; i++) pixelGraph[i] = -1;
//        // Initialising as MST through Prim's
//        HashSet<Integer> visited = new HashSet<>(Grid.pixelArray.length);
//        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>();
//
//        int current = 0;
//        while (visited.size() < Grid.pixelArray.length){
//            if (!visited.contains(current)){
//                visited.add(current);
//                for (int neighbour : Grid.getNeighbourPixels(current)) {
//                    priorityQueue.add(new Edge(current, neighbour));
//                }
//            }
//            Edge edge = priorityQueue.poll();
//            if (!visited.contains(edge.to)){
//                if (pixelGraph[edge.from] == -1) {
//                    System.out.println("hei");
//                    pixelGraph[edge.from] = edge.to;
////                    System.out.println("EDGE: " + edge.from + " " + edge.to);
//                }
//                else {
//                    pixelGraph[edge.to] = edge.from;
////                    System.out.println("EDGE: " + edge.to + " " + edge.from);
//                }
//            }
//            current = edge.to;
//        }
//        for (int i = 0; i < pixelGraph.length; i++)
//            if (pixelGraph[i] == -1) pixelGraph[i] = i;
//
//    }

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


    private ArrayList<Edge> calculateEdges() {
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

    private void printSegments(int initId) {
        System.out.print(initId + "\t");
        for (int j = 0; j < pixelSegments.length; j++) {
            if (pixelSegments[j] == -1) System.out.print("-\t");
            else System.out.print(pixelSegments[j] + "\t");
        }
        System.out.println("\n");
    }

    private void printGraph() {
        for (int i = 0; i < pixelGraph.length; i++) {
            System.out.print(pixelGraph[i] + " ");
        }
        System.out.println();
    }

    private void printDistance(boolean printEdges) {
        double totalDistance = 0;
        ArrayList<Edge> edges = calculateEdges();
        for (Edge edge : edges) {
            totalDistance += edge.weight;
            if (printEdges) System.out.println(edge);
        }
        System.out.println("Distance: " + totalDistance);
    }

}

