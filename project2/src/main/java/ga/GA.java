package ga;

import representation.*;
import tools.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class GA {

    private String mapName;
    private int popSize, maxIterations, eliteAmount, tournamentSize;
    private double elitePercent, crossoverRate;
    private Random random;

    public GA() {
        // SETTINGS
        mapName = Settings.mapName;
        popSize = Settings.popSize;
        maxIterations = Settings.maxIterations;
        elitePercent = Settings.elitePercent;
        tournamentSize = Settings.tournamentSize;
        crossoverRate = Settings.crossoverRate;
        eliteAmount = (int)(popSize * elitePercent / 100.0) + 1;
    }

    private void runAlgorithm() throws IOException, InterruptedException {
        Map map = DataReader.readMapData(mapName);
        random = new Random();
        ArrayList<Solution> population = new ArrayList<>();

        // INITIAL POPULATION
        for (int i = 0; i < popSize*10; i++) {
            population.add(new Solution(map));
        }
        Plotter plotter = new Plotter(map);
        Collections.sort(population);
        population = new ArrayList<>(population.subList(0, population.size()/10));
        Solution bestSolution = population.get(0);

        // EVOLUTION
        for (int i = 0; i < maxIterations; i++) {

            // Add best solution from last population
            ArrayList<Solution> newPopulation = new ArrayList<>(population.subList(0, 1));

            // Add mutated clones of elite set
            ArrayList<Solution> elite = new ArrayList<>(population.subList(0, eliteAmount));
            for (int j = 0; j < elite.size(); j++) {
                newPopulation.add(new Solution(elite.get(j), true));
//                Solution parent1 = tournamentSelection(tournamentSize, elite);
//                Solution parent2 = tournamentSelection(tournamentSize, elite);
//                newPopulation.add(new Solution(parent1, parent2, true));
            }
            // Add offspring from tournament selection to current population
            ArrayList<Solution> offspring = new ArrayList<>();
            while (offspring.size() < popSize) {
                double randDouble = random.nextDouble();
                if (randDouble < crossoverRate) {
                    // Crossover
                    Solution parent1 = tournamentSelection(tournamentSize, population);
                    Solution parent2 = tournamentSelection(tournamentSize, population);
                    offspring.add(new Solution(parent1, parent2, true));
                }
                else {
                    // Clone
                    offspring.add(new Solution(tournamentSelection(tournamentSize, population), true));
                }
            }
            population.addAll(offspring);
            Collections.sort(population);

            // Select next population from current
            while (newPopulation.size() < popSize) {
                double chooseProb = 2/(double)population.size();
                double randomDouble;
                for (int j = 0; j < population.size(); j++) {
                    randomDouble = random.nextDouble();
                    if (randomDouble < chooseProb) {
                        newPopulation.add(population.get(j));
                        population.remove(j);
                        break;
                    }
                }
            }

            population = newPopulation;
            Collections.sort(population);
            bestSolution = population.get(0);

            // PRINT AND WRITE FILE
            double percentOverOptimal = (bestSolution.getTotalDistance()/map.optimalDistance)*100-100;
            System.out.print((i+1) + "\t\t");
            System.out.print("Distance: " + (int)(bestSolution.getTotalDistance()) + "\t\t");
            System.out.print("Cost: " + (int)bestSolution.getCost() + "\t\t");
            System.out.println(String.format(Locale.US, "%.1f", percentOverOptimal) + "% over optimal");
            DataReader.writeSolutionToFile(bestSolution);
            plotter.plotSolution(bestSolution);
        }
        // FINAL PRINT AND WRITE FILE
        TimeUnit.MILLISECONDS.sleep(200);
        plotter.plotSolution(bestSolution);
        System.out.println("-------------------------------");
        System.out.println("Optimal: " + map.optimalDistance);
        System.out.println("Achieved: " + String.format(Locale.US, "%.2f", bestSolution.getTotalDistance()));
        DataReader.writeSolutionToFile(bestSolution);
    }

//    private void runAlgorithm() throws IOException, InterruptedException {
//        Map map = DataReader.readMapData(mapName);
//        random = new Random();
//        ArrayList<Solution> population = new ArrayList<>();
//
//    // INITIAL POPULATION
//        for (int i = 0; i < popSize*10; i++) {
//            population.add(new Solution(map));
//        }
//        Plotter plotter = new Plotter(map);
//        Collections.sort(population);
//        population = new ArrayList<>(population.subList(0, population.size()/10));
//        Solution bestSolution = population.get(0);
//
//    // EVOLUTION
//        for (int i = 0; i < maxIterations; i++) {
//
//            // Add best solution from last population
//            ArrayList<Solution> newPopulation = new ArrayList<>(population.subList(0, 1));
//
//            // Add mutated clones of elite set
//            ArrayList<Solution> elite = new ArrayList<>(population.subList(0, eliteAmount));
//            for (int j = 0; j < elite.size(); j++) {
//                newPopulation.add(new Solution(elite.get(j), true));
////                Solution parent1 = tournamentSelection(tournamentSize, elite);
////                Solution parent2 = tournamentSelection(tournamentSize, elite);
////                newPopulation.add(new Solution(parent1, parent2, true));
//            }
//            // Add offspring from tournament selection cloning
//            while (newPopulation.size() < popSize) {
//                double randDouble = random.nextDouble();
//                if (randDouble < crossoverRate) {
//                    // Crossover
//                    Solution parent1 = tournamentSelection(tournamentSize, population);
//                    Solution parent2 = tournamentSelection(tournamentSize, population);
//                    newPopulation.add(new Solution(parent1, parent2, true));
//                }
//                else newPopulation.add(new Solution(tournamentSelection(tournamentSize, population), true));
//
//            }
//            population = newPopulation;
//            Collections.sort(population);
//            bestSolution = population.get(0);
//
//        // PRINT AND WRITE FILE
//            double percentOverOptimal = (bestSolution.getTotalDistance()/map.optimalDistance)*100-100;
//            System.out.print((i+1) + "\t\t");
//            System.out.print("Distance: " + (int)(bestSolution.getTotalDistance()) + "\t\t");
//            System.out.print("Cost: " + (int)bestSolution.getCost() + "\t\t");
//            System.out.println(String.format(Locale.US, "%.1f", percentOverOptimal) + "% over optimal");
//            DataReader.writeSolutionToFile(bestSolution);
//            plotter.plotSolution(bestSolution);
//        }
//    // FINAL PRINT AND WRITE FILE
//        TimeUnit.MILLISECONDS.sleep(200);
//        plotter.plotSolution(bestSolution);
//        System.out.println("-------------------------------");
//        System.out.println("Optimal: " + map.optimalDistance);
//        System.out.println("Achieved: " + String.format(Locale.US, "%.2f", bestSolution.getTotalDistance()));
//        DataReader.writeSolutionToFile(bestSolution);
//    }

    private Solution findBestSolution(ArrayList<Solution> population) {
        Solution bestSolution = population.get(0);
        double bestSolutionCost = bestSolution.getCost();
        for (int i = 1; i < population.size(); i++) {
            if (population.get(i).getCost() < bestSolutionCost) {
                bestSolution = population.get(i);
                bestSolutionCost = population.get(i).getCost();
            }
        }
        return bestSolution;
    }

    private ArrayList<Solution> clonePopulation(Solution solution) {
        ArrayList<Solution> clonedPopulation = new ArrayList<>();
        clonedPopulation.add(new Solution(solution, false));
        for (int i = 1; i < popSize; i++)
            clonedPopulation.add(new Solution(solution, true));
        return clonedPopulation;
    }

    private Solution tournamentSelection(int tournamentSize, ArrayList<Solution> population) {
        ArrayList<Solution> tournamentSelection = new ArrayList<>();
        for (int j = 0; j < tournamentSize; j++)
            tournamentSelection.add(population.get(random.nextInt(population.size())));
        return findBestSolution(tournamentSelection);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        GA ga = new GA();
        ga.runAlgorithm();
    }
}


