package ga;


import representation.Grid;
import representation.Pixel;
import utility.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Chromosome implements Comparable<Chromosome>{

    // TODO Run when necessary after mutate/crossover: Check if segments have been split by BFS and seeing if there are unvisited pixels in segment. If so, split to new, continue.

    public HashSet<Segment> segments;

    public Chromosome() {
        segments = initialiseSegments();
        mergeSegments(segments);
    }

    public Chromosome(Chromosome clonedFromChromosome) {
        segments = new HashSet<>();
        for (Segment segment : clonedFromChromosome.segments) {
            this.segments.add(segment);
        }
    }

    private HashSet<Segment> initialiseSegments() {
        HashSet<Segment> newSegments = new HashSet<>();

        HashSet<Pixel> unsegmentedPixels = new HashSet<>();
        for (Pixel[] pixelRow : Grid.pixelArray) {
            unsegmentedPixels.addAll(Arrays.asList(pixelRow));
        }

        ArrayList<Pixel> queue = new ArrayList<>();

        while (unsegmentedPixels.size() > 0) {
            Segment segment = new Segment(this);
            // Find random pixel
            Pixel randomPixel = segment.findRandomPixel(unsegmentedPixels);
            unsegmentedPixels.remove(randomPixel);
            segment.addPixel(randomPixel);
            queue.add(randomPixel);
            // Loop and check neighbour pixels
            double initSegmentThresholdPotentialVariance = Settings.initSegmentDistThreshold * Settings.initSegmentDistThresholdVariance;
            double distThresholdVariance = Tools.random.nextDouble() * (initSegmentThresholdPotentialVariance * 2)
                    - initSegmentThresholdPotentialVariance;
            System.out.println(distThresholdVariance);
            while (queue.size() > 0) {
                Pixel pixel = queue.remove(0);
                for (Pixel nbPixel : Grid.getNeighbourPixels(pixel)) {
                    double nbDistance = Tools.rgbDistance3D(nbPixel, segment.calculateAverageRgb());
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
        for (Segment nbSegment : segment.findNeighbourSegments()) {
            double dist = Tools.rgbDistance3D(segment.calculateAverageRgb(), nbSegment.calculateAverageRgb());
            if (dist < closestDist) {
                closestDist = dist;
                closestSegment = nbSegment;
            }
        }
        return closestSegment;
    }

    public void mutate() {
        if (Tools.random.nextDouble() < Settings.mutationRate) {
            // TODO probability distribution between mutate functions
        }
    }


    Segment findRandomSegment(HashSet<Segment> segments) {
        int randIndex = Tools.random.nextInt(segments.size());
        for (Segment segment : segments) {
            if (randIndex == 0) return segment;
            randIndex--;
        }
        return null;
    }

    Segment findPixelSegment(Pixel pixel) {
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

    private double calculateColorDistance() {
        double totalColorDistance = 0;
        for (Segment segment : segments) {
            totalColorDistance += segment.calculateColorDistance();
        }
        return totalColorDistance;
    }

    private double calculateCost() {
        return calculateColorDistance();
    }

    @Override
    public int compareTo(Chromosome o) {
        //TODO Burde bli kalkulert 1 gang per runde og så henter denne bare verdien
        if (this.calculateCost() > o.calculateCost()) return 1;
        else if (this.calculateCost() < o.calculateCost()) return -1;
        return 0;
    }
}
