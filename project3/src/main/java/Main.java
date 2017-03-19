import ga.Chromosome;
import ga.NSGA2;
import ga.Settings;
import utility.ImageReader;
import utility.ImageWriter;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        switch (Settings.algorithm) {
            case NSGA2:
                NSGA2 nsga2 = new NSGA2();
                nsga2.runAlgorithm();
                break;
            case PAES:
                break;
            case TEST:
                System.out.println("Image: " + Settings.imageId);
                ImageReader.readImage(Settings.imageId);
                ImageWriter.writeGridImage();

                Chromosome chromosome = new Chromosome();
                chromosome.removeKRandomEdges(100);
                ImageWriter.writeChromosomeImageRandomRgb(chromosome, 0);
                break;
        }
    }
}
