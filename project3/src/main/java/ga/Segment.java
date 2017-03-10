package ga;

import representation.*;
import java.util.ArrayList;
import java.util.HashSet;


// TODO Sjekke om det er noen begrensninger med å bruke HashSet for Segment
public class Segment {

    HashSet<Pixel> pixels;
    ArrayList<Pixel> border;

    public Segment(HashSet<Pixel> pixels, Pixel initBorderPixel) {
        this.pixels = pixels;
        this.border = findBorder(initBorderPixel);
    }

    private ArrayList<Pixel> findBorder(Pixel initBorderPixel) {
        ArrayList<Pixel> newBorder = new ArrayList<>();
        
        return newBorder;
    }

}
