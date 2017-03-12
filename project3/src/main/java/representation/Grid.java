package representation;


import java.util.HashSet;

public class Grid {

    public static Pixel[][] pixelArray;
    public static int height, width;
    private static int[][] cardinalCoordsArray = {{0,-1}, {1,0}, {0,1}, {-1,0}}; // N, E, S, W

    public static HashSet<Pixel> getNeighbourPixels(Pixel pixel) {
        HashSet<Pixel> neighbourPixels = new HashSet<>();
        for (int i = 0; i < cardinalCoordsArray.length; i++) {
            int nbX = pixel.x + cardinalCoordsArray[i][0];
            int nbY = pixel.y + cardinalCoordsArray[i][1];
            if (nbX >= 0 && nbX < width && nbY >= 0 && nbY < height)
                neighbourPixels.add(pixelArray[nbX][nbY]);
        }
        return neighbourPixels;
    }
}
