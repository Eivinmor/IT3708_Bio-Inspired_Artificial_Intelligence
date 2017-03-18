package representation;

import utility.Tools;

public class Edge implements Comparable<Edge>{
    public int from, to;
    private double weight;

    public Edge(int from, int to) {
        this.from = from;
        this.to = to;
        this.weight = Tools.colorDistance(Grid.pixelArray[from], Grid.pixelArray[to]);
    }

    @Override
    public int compareTo(Edge o) {
        if (this.weight > o.weight) return 1;
        if (this.weight < o.weight) return -1;
        return 0;
    }

    @Override
    public String toString() {
        return "(" + from + " " + to + ")";
    }
}
