package representation;


import java.awt.*;
import java.util.HashSet;

public class Grid {

    public static Color[] pixelArray;
    public static int height, width;
    private static int[] cardinalCoordsArray = {-width, 1, width, 1}; // N, E, S, W

    public HashSet<Integer> getNeighbourPixels(int pixelId) {
        HashSet<Integer> neighbourPixels = new HashSet<>();
        if (Math.floorMod(pixelId, width) != 0) neighbourPixels.add(pixelId - 1);
        if (Math.floorMod(pixelId, width) != width - 1) neighbourPixels.add(pixelId + 1);
        if (pixelId >= width) neighbourPixels.add(pixelId - width);
        if (pixelId < width * (height - 1)) neighbourPixels.add(pixelId + width);
        return neighbourPixels;
    }


}
