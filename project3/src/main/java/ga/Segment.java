package ga;

import representation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;


// TODO Implementere overlapping edges - intersect pixels in region A and B to find edge.
// TODO Sjekke om det er noen begrensninger med å bruke HashSet for Segment

public class Segment {

    private Grid grid;


    public HashSet<Pixel> pixels;
    private ArrayList<Segment> adjacentTo;  //Region Adjacency Graph (RAG)
    public int[] totalRgb;

    public Segment(Grid grid, HashSet<Pixel> pixels) {
        this.pixels = pixels;
        totalRgb = calculateTotalRgb();
    }

    private double calculateColorDistance() {
        double[] averageRgb = calculateAverageRgb();
        double totalDistance = 0;
        for (Pixel pixel : pixels) {
            double distRed = averageRgb[0] - pixel.rgb.getRed();
            double distGreen = averageRgb[1] - pixel.rgb.getGreen();
            double distBlue = averageRgb[2] - pixel.rgb.getBlue();
            totalDistance += Math.sqrt(Math.pow(distRed, 2) + Math.pow(distGreen, 2) + Math.pow(distBlue, 2));
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

    public void removePixel(Pixel pixel) {
        pixels.remove(pixel);
        totalRgb[0] -= pixel.rgb.getRed();
        totalRgb[1] -= pixel.rgb.getGreen();
        totalRgb[2] -= pixel.rgb.getBlue();
    }


}
