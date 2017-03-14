package representation;

import utility.Tools;
import java.awt.*;
import java.util.ArrayList;
import java.util.TreeMap;


public class Grid {

    public static Color[] pixelArray;
    public static int height, width;
//    public static TreeMap<Double, ArrayList<Integer[]>> pixelNeighbourDistances;

    public static ArrayList<Integer> getNeighbourPixels(int pixelId) {
        ArrayList<Integer> neighbourPixels = new ArrayList<>();
        if (pixelId >= width) neighbourPixels.add(pixelId - width);
        if (Math.floorMod(pixelId, width) != width - 1) neighbourPixels.add(pixelId + 1);
        if (pixelId < width * (height - 1)) neighbourPixels.add(pixelId + width);
        if (Math.floorMod(pixelId, width) != 0) neighbourPixels.add(pixelId - 1);
        return neighbourPixels;
    }

//    public static double[][] calculatePixelNeighbourDistances() {
//        double[][] neighbourDistances = new double[pixelArray.length][Settings.numOfNeighbours];
//
//        for (int pixelId = 0; pixelId < pixelArray.length; pixelId++) {
//            int counter = 0;
//            for (Integer neighbourId : getNeighbourPixels(pixelId)) {
//                neighbourDistances[pixelId][counter] = Tools.rgbDistance(pixelArray[pixelId], pixelArray[neighbourId]);
//                counter++;
//            }
//        }
//        return neighbourDistances;
//    }

//    public static TreeMap<Double, ArrayList<Integer[]>> calculatePixelNeighbourDistances() {
//        TreeMap<Double, ArrayList<Integer[]>> distances = new TreeMap<>();
//        for (int pixelId = 0; pixelId < pixelArray.length; pixelId++) {
//            for (Integer neighbourId : getNeighbourPixels(pixelId)) {
//                Double key = Tools.rgbDistance(pixelArray[pixelId], pixelArray[neighbourId]);
//                if (!distances.containsKey(key)) distances.put(key, new ArrayList<>());
//                distances.get(key).add(new Integer[]{pixelId, neighbourId});
//            }
//        }
//        return distances;
//    }





}
