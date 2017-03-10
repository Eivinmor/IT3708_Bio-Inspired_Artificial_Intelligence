package ga;

import representation.*;
import java.util.ArrayList;
import java.util.HashSet;


// TODO Implementere overlapping edges - intersect pixels in region A and B to find edge.
// TODO Sjekke om det er noen begrensninger med å bruke HashSet for Segment

public class Segment {

    private Grid grid;
    private HashSet<Pixel> pixels;
    private ArrayList<Segment> adjacentTo;  //Region Adjacency Graph (RAG)

    public Segment(Grid grid, HashSet<Pixel> pixels) {
        this.pixels = pixels;
    }

}
