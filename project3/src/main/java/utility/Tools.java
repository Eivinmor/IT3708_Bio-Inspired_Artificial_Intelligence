package utility;

import ga.Chromosome;
import ga.Settings;
import representation.Edge;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public abstract class Tools {

    private static CIELab cieLab = new CIELab();
    public static Random random = new Random();
    public static Scanner scanner = new Scanner(System.in);
    public static Plotter plotter = new Plotter();

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
        for (int j = 0; j < chromosome.segmentation.length; j++) {
            if (chromosome.segmentation[j] == -1) System.out.print(" ");
            else System.out.print(chromosome.segmentation[j] + " ");
        }
        System.out.println("\n");
    }

    public static void printGraph(Chromosome chromosome) {
        for (int i = 0; i < chromosome.graph.length; i++) {
            System.out.print(chromosome.graph[i] + " ");
        }
        System.out.println();
    }

    public static double costDistance(Chromosome c1, Chromosome c2) {
        return Math.sqrt(Math.pow(c1.cost[0] - c2.cost[0], 2)
                + Math.pow(c1.cost[1] - c2.cost[1], 2)
                + Math.pow(c1.cost[2] - c2.cost[2], 2));
    }

    public static void printCost(Chromosome chromosome) {
        System.out.println(chromosome.cost[0] + " " + chromosome.cost[1] + " " + chromosome.cost[2]);
    }

    public static void printPause(int generation, Chromosome bestChromosome) {
        ImageWriter.writeChromosomeImageRandomRgb(bestChromosome, generation);
        System.out.println("Generation: " + generation);
        System.out.println("Press Enter to continue...");
        if (scanner.nextLine().equals("gif")) ImagesToGif.readImages();
    }

}
