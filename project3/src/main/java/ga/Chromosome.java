package ga;


import representation.Grid;
import representation.Pixel;
import utility.Formulas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Chromosome {

    public final Grid grid;
    public ArrayList<Segment> segments;

    public Chromosome(Grid grid) {
        this.grid = grid;
        segments = new ArrayList<>();

        HashSet<Pixel> unsegmentedPixels = new HashSet<>();
        for (Pixel[] pixelRow : grid.pixelArray) {
            unsegmentedPixels.addAll(Arrays.asList(pixelRow));
        }

        Random random = new Random();
        ArrayList<Pixel> queue = new ArrayList<>();

        while (unsegmentedPixels.size() > 0) {
            Segment segment = new Segment(grid);
            // Find random pixel
            int randIndex = random.nextInt(unsegmentedPixels.size());
            for (Pixel pixel : unsegmentedPixels) {
                if (randIndex == 0) {
                    unsegmentedPixels.remove(pixel);
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
                    if (nbDistance < Settings.initSegmentDistThreshold && unsegmentedPixels.contains(nbPixel)) {
                        queue.add(nbPixel);
                        unsegmentedPixels.remove(nbPixel);
                        segment.addPixel(nbPixel);
                    }
                }
            }
            segments.add(segment);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Segment segment : segments) {
            sb.append(segment.toString() + "\n");
        }
        return sb.toString();
    }
}
