import ga.Chromosome;
import ga.nsga2.NSGA2;
import ga.Settings;
import utility.ImageReader;
import utility.ImageWriter;
import utility.Plotter;

public class Main {

    public static void main(String[] args) throws InterruptedException {
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

                Plotter plotter = new Plotter();
                Chromosome origChromosome = new Chromosome();
                Chromosome chromosome = new Chromosome(origChromosome);
                for (int i = 0; i < 200; i++) {
                    chromosome = new Chromosome(origChromosome);
                    chromosome.removeKRandomEdges(1000);
                    chromosome.calculateCost();
                    plotter.addChromosome(chromosome);
                }
                ImageWriter.writeChromosomeImageRandomRgb(chromosome, 0);
                plotter.plot();
        }
    }
}
