package task2;

import task1.BaselineAgent;

import java.util.HashMap;
import java.util.Random;


class SupervisedNeuralAgent {

    private task1.World world;
    private task1.BaselineAgent teacher;
    private Random random;
    private int score;
    private double learningRate;
    private int[][] inputLayer;
    private double[] outputLayer;
    private double[][][] weights;
    private HashMap<Character, Integer> inputLayerStatusIndex;

    SupervisedNeuralAgent(){
        random = new Random();
        score = 0;
        learningRate = 0.01;
        double maxStartWeight = 0.001;

        inputLayer = new int[3][4];
        outputLayer = new double[3];
        weights = new double[3][4][3];
        generateStartWeights(0, maxStartWeight);

        inputLayerStatusIndex = new HashMap<>(4);
        inputLayerStatusIndex.put(' ', 0);
        inputLayerStatusIndex.put('W', 1);
        inputLayerStatusIndex.put('F', 2);
        inputLayerStatusIndex.put('P', 3);
        teacher = new BaselineAgent();
    }

    private char[] observe(){
        char[] observations = new char[3];   // L, F, R
        for (int i = 0; i < 3; i++) {
            observations[i] = world.observeInDirection(i);
        }
        return observations; // L, F, R
    }

    int chooseMoveDirection(char[] observations){
        activateNetwork(observations);
        int bestDirection = -1;
        double bestValue = Double.MIN_VALUE;
        for (int i = 0; i < 3; i++) {
//            System.out.println(outputLayer[i]);
            if (outputLayer[i] > bestValue) {
                bestDirection = i;
                bestValue = outputLayer[i];
            }
        }
//        System.out.println("Chose direction: " + bestDirection);
        return bestDirection;
    }

    void activateNetwork(char[] observations){
//        for (int i = 0; i < 3; i++) {
//            int observedStatusIndex = inputLayerStatusIndex.get(observations[i]);   // gets input layer index of status from observaron
//            for (int j = 0; j < 4; j++) {
//                if (j == observedStatusIndex) inputLayer[i][j] = 1;
//                else inputLayer[i][j] = 0;
//                for (int k = 0; k < 3; k++) {
//                    outputLayer[k] += weights[i][observedStatusIndex][k];
//                }
//            }
//
//        }
        resetInputLayer();
        resetOutputLayer();
        for (int i = 0; i < 3; i++) {
            int observedStatusIndex = inputLayerStatusIndex.get(observations[i]);   // gets input layer index of status from observaron
            inputLayer[i][observedStatusIndex] = 1;
            for (int j = 0; j < 3; j++) {
                outputLayer[j] += weights[i][observedStatusIndex][j];
            }
        }
    }

    void updateWeights(int moveDirection, int teacherDirection){
        double sumExpOutput = 0;
        for (int i = 0; i < 3; i++) {
            sumExpOutput += Math.exp(outputLayer[i]);
        }
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    int correctChoice = 0;
                    if (teacherDirection == k) correctChoice = 1;
                    weights[i][j][k] += learningRate * deltaRule(k, correctChoice, sumExpOutput) * inputLayer[i][j];
                }
                
            }
        }
    }

    private double deltaRule(int outputNeuron, int correctChoice, double sumExpOutput) {
        double actualExpOutput = Math.exp(outputLayer[outputNeuron]);
        return - (actualExpOutput/sumExpOutput) + (double)correctChoice;
    }

    private void resetInputLayer(){
        for (int i = 0; i < inputLayer.length; i++) {
            for (int j = 0; j < inputLayer[i].length; j++) {
                inputLayer[i][j] = 0;
            }
        }
    }

    private void resetOutputLayer(){
        for (int i = 0; i < outputLayer.length; i++) {
            outputLayer[i] = 0;
        }
    }

    int getScore(){return score;}

    void registerNewWorld(task1.World newWorld){
        world = newWorld;
        score = 0;
    }

    void generateStartWeights(double low, double high){
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    weights[i][j][k] = (random.nextDouble() * ((high - low) + low));
                }
            }
        }
    }

    void printWeights(){
        System.out.format("%24s\n", "OUTPUT");
        System.out.format("%16s%10s%10s", "Left", "Forward", "Right");
        System.out.println();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.format("%6s", "");
                for (int k = 0; k < 3; k++) {
                    System.out.format("%10f", Math.round(weights[i][j][k]*1000.0)/1000.0);
                }
                System.out.println();
            }
            System.out.println();
        }
        System.out.println("------------------------------\n");
    }

    void step() {
        char[] observations = observe();
        int chosenMoveDirection = chooseMoveDirection(observations);
        int teacherDirection = teacher.chooseMoveDirection(observations);
        updateWeights(chosenMoveDirection, teacherDirection);
//        printWeights();
        score += world.moveAgent(chosenMoveDirection);
    }
}
