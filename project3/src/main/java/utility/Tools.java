package utility;

import representation.Pixel;

import java.util.Random;


public class Tools {

    public static Random random = new Random();

    public static double rgbDistance3D(double dist1, double dist2, double dist3) {
        return Math.sqrt(Math.pow(dist1, 2) + Math.pow(dist2, 2) + Math.pow(dist3, 2));
    }

    public static double rgbDistance3D(double a1, double a2, double a3, double b1, double b2, double b3) {
        return Math.sqrt(Math.pow(a1 - b1, 2) + Math.pow(a2 - b2, 2) + Math.pow(a3 - b3, 2));
    }

    public static double rgbDistance3D(Pixel p1, Pixel p2) {
        return Math.sqrt(Math.pow(p1.rgb.getRed() - p2.rgb.getRed(), 2)
                + Math.pow(p1.rgb.getGreen() - p2.rgb.getGreen(), 2)
                + Math.pow(p1.rgb.getBlue() - p2.rgb.getBlue(), 2));
    }

    public static double rgbDistance3D(Pixel pixel, double[] segmentAverageRgb) {
        double distRed = segmentAverageRgb[0] - pixel.rgb.getRed();
        double distGreen = segmentAverageRgb[1] - pixel.rgb.getGreen();
        double distBlue = segmentAverageRgb[2] - pixel.rgb.getBlue();
        return Math.sqrt(Math.pow(distRed, 2) + Math.pow(distGreen, 2) + Math.pow(distBlue, 2));
    }

    public static double rgbDistance3D(double[] segment1AverageRgb, double[] segment2AverageRgb) {
        double distRed = segment1AverageRgb[0] - segment2AverageRgb[0];
        double distGreen = segment1AverageRgb[1] - segment2AverageRgb[1];
        double distBlue = segment1AverageRgb[2] - segment2AverageRgb[2];
        return Math.sqrt(Math.pow(distRed, 2) + Math.pow(distGreen, 2) + Math.pow(distBlue, 2));
    }

}
