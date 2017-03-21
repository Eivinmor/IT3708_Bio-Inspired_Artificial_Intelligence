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
            case PAES:
                break;
            case TEST:
                Chromosome origChromosome = new Chromosome();
                Chromosome chromosome = new Chromosome(origChromosome);
                chromosome.removeKRandomEdges(20000);
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < 1000; i++) {
                    chromosome.overallColorDeviation();
                }
                System.out.println(System.currentTimeMillis() - startTime);
//                for (int i = 0; i < 200; i++) {
//                    chromosome = new Chromosome(origChromosome);
//                    chromosome.removeKRandomEdges(1000);
//                    chromosome.calculateCost();
//                    plotter.addChromosome(chromosome);
//                }
//                ImageWriter.writeChromosomeImageRandomRgb(chromosome, 0);
//                plotter.plot();
        }
    }
}
