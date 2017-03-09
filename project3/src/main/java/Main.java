import representation.Grid;
import utility.ImageReader;
import utility.ImageWriter;

public class Main {

    public static void main(String[] args) {
        Grid grid = ImageReader.readImage(1);
        ImageWriter.writeImage(grid);
    }

}
