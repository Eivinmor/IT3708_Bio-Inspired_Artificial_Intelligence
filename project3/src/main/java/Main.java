import ga.Chromosome;
import ga.nsga2.NSGA2;
import ga.Settings;
import representation.Grid;
import utility.ImageReader;
import utility.ImageWriter;
import utility.Tools;


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
                Chromosome chromosome = new Chromosome();
                chromosome.removeKRandomEdges(2000);
                chromosome.mergeSegmentsSmallerThanK(1000);
                ImageWriter.writeChromosomeImageWithEdges(chromosome, 0);
                ImageWriter.writeChromosomeEdgesBlackOnWhite(chromosome, 1);
                ImageWriter.writeChromosomeImageRandomRgb(chromosome, 2);
//                double totalDist = 0;
//                for (int i = 0; i < chromosome.graph.length; i++) {
//                    totalDist += Tools.colorDistance(Grid.pixelArray[i], Grid.pixelArray[chromosome.graph[i]]);
//                }
//                System.out.println(totalDist);
        }
    }
}
