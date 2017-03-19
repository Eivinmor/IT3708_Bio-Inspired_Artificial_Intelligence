import ga.Chromosome;
import ga.Settings;
import utility.ImageReader;
import utility.ImageWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Image: " + Settings.imageId);
        ImageReader.readImage(Settings.imageId);
        ImageWriter.writeGridImage();
//        Chromosome chromosome1 = new Chromosome();
//        Chromosome chromosome2 = new Chromosome();

//        System.out.println("Overall deviation: " + String.format(Locale.US, "%.2f", chromosome.overallColorDeviation()));
//        System.out.println("Number of pixels: " + chromosome.segmentation.length);
//        System.out.println("Number of segments: " + chromosome.numOfSegments);

//        for (int i = 0; i < 10; i++) {
//            ImageWriter.writeChromosomeImageAvgRgb(new Chromosome(chromosome1, chromosome2), i, false);
//        }

        long startTime = System.currentTimeMillis();
        Chromosome mstChromosome = new Chromosome();
        ArrayList<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < Settings.populationSize; i++) {
            population.add(new Chromosome(mstChromosome));
            population.get(i).removeKRandomEdges(20000);
            population.get(i).calculateCost();
            population.get(i).printCost();
        }
        System.out.println(System.currentTimeMillis() - startTime);

        Collections.sort(population);
        for (int i = 0; i < Settings.populationSize; i++) {
            for (int j = i + 1; j < Settings.populationSize; j++) {
                if (population.get(i).compareTo(population.get(j)) < 0) {
                    System.out.println();
                    population.get(i).printCost();
                    population.get(j).printCost();
                }
            }
//            System.out.println(population.get(i).cost[0] + " " + population.get(i).cost[1] + " " + population.get(i).cost[2]);

        }


//        for (int i = 0; i < Settings.generations; i++) {
//            long startTIme = System.currentTimeMillis();
//            for (int j = 0; j < 50; j++) {
//                chromosome1 = new Chromosome(chromosome1, chromosome2);
//                for (int k = 0; k < 1000; k++) {
//                    chromosome1.mutate();
//                    chromosome2.mutate();
//                }
//                System.out.println("Overall deviation: " + String.format(Locale.US, "%.2f", chromosome1.overallColorDeviation()));
//                System.out.println("Edge value: " + chromosome1.edgeValue());
//                System.out.println("Connectivity: " + chromosome1.connectivity());
//                System.out.println();
//            }
//            ImageWriter.writeChromosomeImageAvgRgb(chromosome1, i, false);
//            System.out.println(System.currentTimeMillis() - startTIme);
//        }
    }

}
