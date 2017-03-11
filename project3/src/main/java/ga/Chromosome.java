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
        ArrayList<Pixel> queue = new ArrayList<>();

        while (pixelHashSet.size() > 0) {
            HashSet<Pixel> segmentPixels = new HashSet<>();

            // Find random pixel
            int randIndex = random.nextInt(pixelHashSet.size());
            for (Pixel pixel : pixelHashSet) {
                if (randIndex == 0) {
                    pixelHashSet.remove(pixel);
                    queue.add(pixel);
                    break;
                }
                randIndex--;
            }

            while (queue.size() > 0) {




            }
            segments.add(new Segment(grid, segmentPixels));
        }



    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }


}
