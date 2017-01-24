package task3;

import java.util.HashMap;
import java.util.Random;


class ReinforcedNeuralAgent {

    private task1.World world;
    private Random random;
    private int score;
    private double learningRate;
    private int[][] inputLayer;
    private double[] outputLayer;
    private double[][][] weights;
    private HashMap<Character, Integer> inputLayerStatusIndex;

    ReinforcedNeuralAgent(){
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
        double bestValue = -Double.MAX_VALUE;
        for (int i = 0; i < 3; i++) {
            if (outputLayer[i] > bestValue) {
                bestDirection = i;
                bestValue = outputLayer[i];
            }
        }
        return bestDirection;
    }

    void activateNetwork(char[] observations){
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

    void updateWeights(int[] newOutputValues){
        double maxNextOutput = -Double.MAX_VALUE;
        for (int i = 0; i < newOutputValues.length; i++) {
            if (newOutputValues[i] > maxNextOutput) maxNextOutput = newOutputValues[i];
        }
    }

    private double deltaRule(int reward, double discountFactor, double maxNextOutput, double oldOutput) {
        return (reward + discountFactor * maxNextOutput - oldOutput);
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

    void generateStartWeights(double low, double high){
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    weights[i][j][k] = (random.nextDouble() * ((high - low) + low));
                }
            }
        }
    }

    void registerNewWorld(task1.World newWorld){
        world = newWorld;
        score = 0;
    }

    public void printWeights(){
        System.out.format("%24s\n", "OUTPUT");
        System.out.format("%22s%16s%16s", "Left", "Forward", "Right");
        System.out.println();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.format("%6s", "");
                for (int k = 0; k < 3; k++) {
                    System.out.format("%16.10f", weights[i][j][k]);
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
        score += world.moveAgent(chosenMoveDirection);
//        updateWeights(chosenMoveDirection);
    }
}
