import representation.Grid;
import utility.ImageReader;
import utility.ImageWriter;

public class Main {

    public static void main(String[] args) {
        int imageId = 1;
        Grid grid = ImageReader.readImage(imageId);
        ImageWriter.writeImage(grid);
    }

}
