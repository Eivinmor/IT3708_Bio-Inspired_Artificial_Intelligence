import ga.Chromosome;
import ga.Settings;
import utility.ImageReader;
import utility.ImageWriter;

public class Main {

    public static void main(String[] args) {
        ImageReader.readImage(Settings.imageId);
        Chromosome chromosome = new Chromosome();
        ImageWriter.writeChromosomeImageAvgRgb(chromosome, 0, true);
        ImageWriter.writeChromosomeImageRandRgb(chromosome, 0, true);
    }

}
