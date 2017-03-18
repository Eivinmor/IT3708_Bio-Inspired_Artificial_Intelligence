package utility;

import ga.Chromosome;
import ga.Settings;
import representation.Edge;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class Tools {

    private static CIELab cieLab = new CIELab();

    public static Random random = new Random();

    public static double euclideanDist3D(double dist1, double dist2, double dist3) {
        return Math.sqrt(Math.pow(dist1, 2) + Math.pow(dist2, 2) + Math.pow(dist3, 2));
    }

    public static double euclideanDist3D(double a1, double a2, double a3, double b1, double b2, double b3) {
        return Math.sqrt(Math.pow(a1 - b1, 2) + Math.pow(a2 - b2, 2) + Math.pow(a3 - b3, 2));
    }

    public static double colorDistance(Color c1, Color c2) {
        switch (Settings.colorSpace) {
            case RGB: return rgbDistance(c1, c2);
            case LAB: return cieLabDistance(c1, c2);
        }
        throw new IllegalArgumentException("Settings.colorSpace value is not recognised.");
    }

    private static double rgbDistance(Color c1, Color c2) {
        return Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2)
                + Math.pow(c1.getGreen() - c2.getGreen(), 2)
                + Math.pow(c1.getBlue() - c2.getBlue(), 2));
    }

    private static double cieLabDistance(Color c1, Color c2) {
        float[] c1Lab = cieLab.fromRGB(c1.getRGBColorComponents(null));
        float[] c2Lab = cieLab.fromRGB(c2.getRGBColorComponents(null));
        return Math.sqrt(Math.pow(c1Lab[0] - c2Lab[0], 2)
                + Math.pow(c1Lab[1] - c2Lab[1], 2)
                + Math.pow(c1Lab[2] - c2Lab[2], 2));
    }

    public static void printDistance(Chromosome chromosome, boolean printEdges) {
        double totalDistance = 0;
        ArrayList<Edge> edges = chromosome.calculateEdges();
        for (Edge edge : edges) {
            totalDistance += edge.weight;
            if (printEdges) System.out.println(edge);
        }
        System.out.println("Distance: " + totalDistance);
    }

    public static void printSegments(Chromosome chromosome) {
        for (int j = 0; j < chromosome.pixelSegments.length; j++) {
            if (chromosome.pixelSegments[j] == -1) System.out.print(" ");
            else System.out.print(chromosome.pixelSegments[j] + " ");
        }
        System.out.println("\n");
    }

    public static void printGraph(Chromosome chromosome) {
        for (int i = 0; i < chromosome.pixelGraph.length; i++) {
            System.out.print(chromosome.pixelGraph[i] + " ");
        }
        System.out.println();
    }
}
