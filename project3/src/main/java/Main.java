import ga.Chromosome;
import ga.Settings;
import utility.ImageReader;
import utility.ImageWriter;
import java.util.Locale;


public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Image: " + Settings.imageId);
        ImageReader.readImage(Settings.imageId);
        ImageWriter.writeGridImage();
        Chromosome chromosome = new Chromosome();
        System.out.println("Overall deviation: " + String.format(Locale.US, "%.2f", chromosome.overallColorDeviation()));
        System.out.println("Number of pixels: " + chromosome.segmentation.length);
        System.out.println("Number of segments: " + chromosome.numOfSegments);
        ImageWriter.writeChromosomeImageAvgRgb(chromosome, 1, false);

//        for (int i = 0; i < Settings.populationSize - 1; i++) {
//            new Chromosome();
//            if (i % 10 == 0) System.out.println("-");
//        }

    }

}
