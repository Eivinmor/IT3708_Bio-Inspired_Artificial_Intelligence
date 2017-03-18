package utility;

import ga.Settings;

import java.awt.*;
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
        if (Settings.colorSpace == 0) return rgbDistance(c1, c2);
        else if (Settings.colorSpace == 1) return cieLabDistance(c1, c2);
        return -1;
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



}
