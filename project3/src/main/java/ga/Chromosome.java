package ga;


import java.util.ArrayList;

public class Chromosome {

    private ArrayList<Segment> segments;

    public Chromosome(ArrayList<Segment> segments) {
        this.segments = segments;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }


}
