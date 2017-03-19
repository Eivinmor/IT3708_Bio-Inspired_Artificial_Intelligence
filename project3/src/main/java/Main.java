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
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();

//        System.out.println("Overall deviation: " + String.format(Locale.US, "%.2f", chromosome.overallColorDeviation()));
//        System.out.println("Number of pixels: " + chromosome.segmentation.length);
//        System.out.println("Number of segments: " + chromosome.numOfSegments);

//        for (int i = 0; i < 10; i++) {
//            ImageWriter.writeChromosomeImageAvgRgb(new Chromosome(chromosome1, chromosome2), i, false);
//        }

        for (int i = 0; i < Settings.generations; i++) {
            long startTIme = System.currentTimeMillis();
            for (int j = 0; j < 50; j++) {
                chromosome1 = new Chromosome(chromosome1, chromosome2);
                for (int k = 0; k < 1000; k++) {
                    chromosome1.mutate();
                    chromosome2.mutate();
                }
                System.out.println("Overall deviation: " + String.format(Locale.US, "%.2f", chromosome1.overallColorDeviation()));
                System.out.println("Edge value: " + chromosome1.edgeValue());
                System.out.println("Connectivity: " + chromosome1.connectivity());
                System.out.println();
            }
            ImageWriter.writeChromosomeImageAvgRgb(chromosome1, i, false);
            System.out.println(System.currentTimeMillis() - startTIme);
        }
    }

}
