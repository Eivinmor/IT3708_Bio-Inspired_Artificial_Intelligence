package ga;

import representation.*;
import utility.Formulas;

import java.util.HashSet;


// TODO Implementere overlapping edges - intersect pixels in region A and B to find edge.
// TODO Sjekke om det er noen begrensninger med å bruke HashSet for Segment

public class Segment {

    private Grid grid;
    private Chromosome chromosome;
    public HashSet<Pixel> pixels;
    public int[] totalRgb;

    public Segment(Grid grid, Chromosome chromosome) {
        this.grid = grid;
        this.chromosome = chromosome;
        pixels = new HashSet<>();
        totalRgb = calculateTotalRgb();

    }

    public HashSet<Segment> findNeighbours() {
        HashSet<Segment> adjacentTo = new HashSet<>();
        for (Pixel pixel : pixels) {
            for (Pixel nbPixel : grid.getNeighbourPixels(pixel)) {
                if (!pixels.contains(nbPixel)) adjacentTo.add(chromosome.findPixelSegment(nbPixel));
            }
        }
        return adjacentTo;
    }

    private double calculateColorDistance() {
        double[] averageRgb = calculateAverageRgb();
        double totalDistance = 0;
        for (Pixel pixel : pixels) {
           totalDistance += Formulas.rgbDistance3D(pixel, averageRgb);
        }
        return totalDistance;
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
