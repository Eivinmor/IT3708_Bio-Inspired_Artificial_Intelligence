package representation;


public class Grid {

    public final Pixel[][] pixelArray;
    public final int height;
    public final int width;

    public Grid(Pixel[][] pixelArray) {
        this.pixelArray = pixelArray;
        this.width = pixelArray.length;
        this.height = pixelArray[0].length;
    }
}
