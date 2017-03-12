package ga;

import representation.*;
import utility.Tools;
import java.util.HashSet;


public class Segment {

    private Chromosome chromosome;
    public HashSet<Pixel> pixels;
    public int[] totalRgb;

    public Segment(Chromosome chromosome) {
        this.chromosome = chromosome;
        pixels = new HashSet<>();
        totalRgb = calculateTotalRgb();
    }

    public Segment(Chromosome chromosome, Segment clonedFromSegment) {
        this.chromosome = chromosome;
        this.pixels = new HashSet<>(clonedFromSegment.pixels);
        this.totalRgb = clonedFromSegment.totalRgb;
    }

    // TODO Fitness functions

    public double calculateColorDistance() {
        double[] averageRgb = calculateAverageRgb();
        double totalDistance = 0;
        for (Pixel pixel : pixels) {
            totalDistance += Tools.rgbDistance3D(pixel, averageRgb);
        }
        return totalDistance;
    }

    public HashSet<Segment> findNeighbourSegments() {
        HashSet<Segment> adjacentTo = new HashSet<>();
        for (Pixel pixel : pixels) {
            for (Pixel nbPixel : Grid.getNeighbourPixels(pixel)) {
                if (!pixels.contains(nbPixel)) adjacentTo.add(chromosome.findPixelSegment(nbPixel));
            }
        }
        return adjacentTo;
    }

    public double[] calculateAverageRgb() {
        double averageRed = (double) totalRgb[0] / pixels.size();
        double averageGreen = (double) totalRgb[1] / pixels.size();
        double averageBlue = (double) totalRgb[2] / pixels.size();
        return new double[] {averageRed, averageGreen, averageBlue};
    }

    private int[] calculateTotalRgb() {
        int[] newTotalRgb = new int[3];
        for (Pixel pixel : pixels) {
            totalRgb[0] += pixel.rgb.getRed();
            totalRgb[1] += pixel.rgb.getGreen();
            totalRgb[2] += pixel.rgb.getBlue();
        }
        return newTotalRgb;
    }

    public Pixel findRandomPixel(HashSet<Pixel> pixels) {
        int randIndex = Tools.random.nextInt(pixels.size());
        for (Pixel pixel : pixels) {
            if (randIndex == 0) return pixel;
            randIndex--;
        }
        return null;
    }

    public void addPixel(Pixel pixel) {
        pixels.add(pixel);
        totalRgb[0] += pixel.rgb.getRed();
        totalRgb[1] += pixel.rgb.getGreen();
        totalRgb[2] += pixel.rgb.getBlue();
    }

    public void addPixels(HashSet<Pixel> pixels) {
        for (Pixel pixel : pixels) {
           addPixel(pixel);
        }
    }

    public void removePixel(Pixel pixel) {
        pixels.remove(pixel);
        totalRgb[0] -= pixel.rgb.getRed();
        totalRgb[1] -= pixel.rgb.getGreen();
        totalRgb[2] -= pixel.rgb.getBlue();
    }

//    @Override
//    public String toString() {
//        return Integer.toString(pixels.size());
//    }
}
