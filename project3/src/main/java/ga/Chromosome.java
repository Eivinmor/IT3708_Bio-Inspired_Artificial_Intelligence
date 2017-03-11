package ga;


import representation.Grid;
import representation.Pixel;
import utility.Formulas;

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
            Segment segment = new Segment(grid);
            // Find random pixel
            int randIndex = random.nextInt(pixelHashSet.size());
            for (Pixel pixel : pixelHashSet) {
                if (randIndex == 0) {
                    pixelHashSet.remove(pixel);
                    segment.addPixel(pixel);
                    queue.add(pixel);
                    break;
                }
                randIndex--;
            }
            // Loop and check neighbour pixels
            while (queue.size() > 0) {
                Pixel pixel = queue.remove(0);
                for (Pixel nbPixel : grid.getNeighbourPixels(pixel)) {
                    double nbDistance = Formulas.rgbDistance3D(nbPixel, segment.calculateAverageRgb());
                    if (nbDistance < Settings.initSegmentDistThreshold) {
                        pixelHashSet.remove(nbPixel);
                        segment.addPixel(nbPixel);
                        queue.add(nbPixel);
                    }
                }
            }
            segments.add(segment);
        }
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }
    
}
