import ga.Chromosome;
import ga.NSGA2;
import ga.Settings;
import utility.ImageReader;
import utility.ImageWriter;
import utility.Plotter;

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

                Plotter plotter = new Plotter();
                Chromosome origChromosome = new Chromosome();
                for (int i = 0; i < 200; i++) {
                    Chromosome chromosome = new Chromosome(origChromosome);
                    chromosome.removeKRandomEdges(10000);
                    chromosome.calculateCost();
                    plotter.addChromosome(chromosome);
                }
                plotter.plot();
                break;
        }
    }
}
