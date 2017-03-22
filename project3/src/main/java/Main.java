import ga.Chromosome;
import ga.nsga2.NSGA2;
import ga.Settings;
import utility.ImageReader;
import utility.ImageWriter;


public class Main {

    public static void main(String[] args) throws InterruptedException {
        ImageWriter.clearFolder();
        System.out.println("Image: " + Settings.imageId);
        ImageReader.readImage(Settings.imageId);
        ImageWriter.writeGridImage();

        switch (Settings.algorithm) {
            case NSGA2:
                NSGA2 nsga2 = new NSGA2();
                nsga2.runAlgorithm();
                break;
            case TEST:
                Chromosome chromosome = new Chromosome();
                for (int i = 1; i < 100; i++) {
                    chromosome.mutateAddNewSegmendWithinThreshold(Settings.mutateAddNewSegmentMaxThreshold);
                    chromosome.segmentationIsOutdated = true;
                    ImageWriter.writeChromosomeImageRandomRgb(chromosome, i);
                }
        }
    }
}
