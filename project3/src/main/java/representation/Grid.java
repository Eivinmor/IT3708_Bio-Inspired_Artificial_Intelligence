package representation;


import java.util.HashSet;

public class Grid {

    public final Pixel[][] pixelArray;
    public final int height, width;
    public final int[][] cardinalCoordsArray;

    public Grid(Pixel[][] pixelArray) {
        this.pixelArray = pixelArray;
        this.width = pixelArray.length;
        this.height = pixelArray[0].length;

        cardinalCoordsArray  = new int[4][2];
        cardinalCoordsArray[0] = new int[] {0,-1};   // N
        cardinalCoordsArray[1] = new int[] {1,0};    // E
        cardinalCoordsArray[2] = new int[] {0,1};    // S
        cardinalCoordsArray[3] = new int[] {-1,0};   // W

    }

    public HashSet<Pixel> getNeighbourPixels(Pixel pixel) {
        HashSet<Pixel> neighbourPixels = new HashSet<>();
        for (int i = 0; i < cardinalCoordsArray.length; i++) {
            int nbX = pixel.x + cardinalCoordsArray[i][0];
            int nbY = pixel.y + cardinalCoordsArray[i][1];
            if (nbX > 0 && nbX < width && nbY > 0 && nbY < height)
                neighbourPixels.add(pixelArray[nbX][nbY]);
        }
        return neighbourPixels;
    }
}
