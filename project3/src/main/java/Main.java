import ga.Chromosome;
import ga.Settings;
import utility.ImageReader;
import utility.ImageWriter;


public class Main {

    public static void main(String[] args) throws InterruptedException {
        ImageReader.readImage(Settings.imageId);
        ImageWriter.writeGridImage();
        Chromosome chromosome = new Chromosome();
        System.out.println("Number of pixels: " + chromosome.pixelSegments.length);
        System.out.println("Number of segments: " + chromosome.numOfSegments);
        ImageWriter.writeChromosomeImageAvgRgb(chromosome, 1, false);

//        for (int i = 0; i < Settings.populationSize - 1; i++) {
//            new Chromosome();
//        }
//        Chromosome chromosome = new Chromosome();
//        ImageWriter.writeChromosomeImageAvgRgb(chromosome, 1, true);
//        ImageWriter.writeChromosomeImageRandRgb(chromosome, 1, true);
    }

}
