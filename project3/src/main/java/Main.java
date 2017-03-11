import ga.Chromosome;
import ga.Settings;
import representation.Grid;
import utility.ImageReader;
import utility.ImageWriter;

public class Main {

    public static void main(String[] args) {
        Grid grid = ImageReader.readImage(Settings.imageId);
        Chromosome chromosome = new Chromosome(grid);
        ImageWriter.writeChromosomeImageAvgRgb(chromosome, 0, true);
        ImageWriter.writeChromosomeImageRandRgb(chromosome, 0, true);
    }

}
