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
    public HashSet<Segment> segments;

    public Chromosome(Grid grid) {
        this.grid = grid;
        segments = initialiseSegments();
        mergeSegments(segments);
    }

    private HashSet<Segment> initialiseSegments() {
        HashSet<Segment> newSegments = new HashSet<>();

        HashSet<Pixel> unsegmentedPixels = new HashSet<>();
        for (Pixel[] pixelRow : grid.pixelArray) {
            unsegmentedPixels.addAll(Arrays.asList(pixelRow));
        }

        Random random = new Random();
        ArrayList<Pixel> queue = new ArrayList<>();

        while (unsegmentedPixels.size() > 0) {
            Segment segment = new Segment(grid, this);
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
            double distThresholdVariance = random.nextDouble() * (Settings.initSegmentDistThreshold * Settings.initSegmentDistThresholdVariance)
                    - (Settings.initSegmentDistThreshold * Settings.initSegmentDistThresholdVariance / 2);
            System.out.println(distThresholdVariance);
            while (queue.size() > 0) {
                Pixel pixel = queue.remove(0);
                for (Pixel nbPixel : grid.getNeighbourPixels(pixel)) {
                    double nbDistance = Formulas.rgbDistance3D(nbPixel, segment.calculateAverageRgb());
                    if (nbDistance < (Settings.initSegmentDistThreshold + distThresholdVariance) && unsegmentedPixels.contains(nbPixel)) {
                        queue.add(nbPixel);
                        unsegmentedPixels.remove(nbPixel);
                        segment.addPixel(nbPixel);
                    }
                }
            }
            newSegments.add(segment);
        }
        return newSegments;
    }

    // TODO Randomise for initial pop diversity?
    private void mergeSegments(HashSet<Segment> segments) {
        ArrayList<Segment> tooSmallSegments = findTooSmallSegments(segments);
        while (tooSmallSegments.size() > 0) {
            Segment segment = tooSmallSegments.remove(0);
            Segment closestSegment = findClosestColorSegment(segment);
            closestSegment.addPixels(segment.pixels);

            segments.remove(segment);
            tooSmallSegments = findTooSmallSegments(segments);
        }
    }

    private ArrayList<Segment> findTooSmallSegments(HashSet<Segment> segments) {
        ArrayList<Segment> tooSmallSegments = new ArrayList<>();
        for (Segment segment : segments) {
            if (segment.pixels.size() < Settings.initSegmentMinimumSize) {
                tooSmallSegments.add(segment);
            }
        }
        return tooSmallSegments;
    }

    private Segment findClosestColorSegment(Segment segment) {
        Segment closestSegment = null;
        double closestDist = Double.MAX_VALUE;
        for (Segment nbSegment : segment.findNeighbours()) {
            double dist = Formulas.rgbDistance3D(segment.calculateAverageRgb(), nbSegment.calculateAverageRgb());
            if (dist < closestDist) {
                closestDist = dist;
                closestSegment = nbSegment;
            }
        }
        return closestSegment;
    }


    public Segment findPixelSegment(Pixel pixel) {
        for (Segment segment : segments) {
            if (segment.pixels.contains(pixel)) return segment;
        }
        return null;
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
