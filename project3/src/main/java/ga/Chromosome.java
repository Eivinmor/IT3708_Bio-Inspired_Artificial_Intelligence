package ga;


import representation.Grid;
import representation.Pixel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Chromosome {

    private final Grid grid;
    private ArrayList<Segment> segments;

    public Chromosome(Grid grid) {
        this.grid = grid;

        HashSet<Pixel> pixelHashSet = new HashSet<>();
        for (Pixel[] pixelRow : grid.pixelArray) {
            pixelHashSet.addAll(Arrays.asList(pixelRow));
        }
        Random random = new Random();

        int x = random.nextInt(grid.width);
        int y = random.nextInt(grid.height);

        ArrayList<Pixel> queue = new ArrayList<>();


    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }


}
