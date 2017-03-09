package representation;


public class Grid {

    public final Pixel[][] pixelArray;
    public final int height;
    public final int width;

    public Grid(Pixel[][] pixelArray) {
        this.pixelArray = pixelArray;
        this.height = pixelArray.length;
        this.width = pixelArray[0].length;
    }
}
