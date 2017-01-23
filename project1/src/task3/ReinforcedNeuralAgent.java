package task3;

import task1.BaselineAgent;

import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Random;


class ReinforcedNeuralAgent {

    private task1.World world;
    private Random random;
    private int score, oldMoveDirection, oldReward;
    private double learningRate, discountFactor;
    private int[][] inputLayer, oldInputLayer;
    private double[] outputLayer, oldOutputLayer;
    private double[][][] weights;
    private HashMap<Character, Integer> inputLayerStatusIndex;
    private boolean firstStep;

    ReinforcedNeuralAgent(){
        random = new Random();
        score = 0;
        learningRate = 0.01;
        discountFactor = 0.9;
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

        firstStep = true;
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
//            System.out.println(String.format("%.1f",outputLayer[i]));
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
            int observedStatusIndex = inputLayerStatusIndex.get(observations[i]);   // gets input layer index of status
            inputLayer[i][observedStatusIndex] = 1;
            for (int j = 0; j < 3; j++) {
                outputLayer[j] += weights[i][observedStatusIndex][j];
            }
        }
    }

    void updateWeights(){
        double maxOutput = -Double.MAX_VALUE;
        for (int i = 0; i < 3; i++) {
            if (outputLayer[i] > maxOutput) maxOutput = outputLayer[i];
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4 && oldInputLayer[i][j]==1; j++) {
                weights[i][j][oldMoveDirection] +=
                        learningRate * deltaRule(maxOutput) * oldInputLayer[i][j];
            }
        }
    }

    private double deltaRule(double maxOutput) {
        System.out.println(oldOutputLayer[oldMoveDirection]);
        return oldReward + discountFactor * maxOutput - oldOutputLayer[oldMoveDirection];
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
        char[] observations = observe();                                // s'
        int chosenMoveDirection = chooseMoveDirection(observations);    // a'
        int reward = world.moveAgent(chosenMoveDirection);
        score += reward;
        if (!firstStep){
            updateWeights();                                            // --
        }
        else firstStep = false;
        oldReward = reward;                                             // r
        oldInputLayer = inputLayer.clone();                             // s
        oldMoveDirection = chosenMoveDirection;                         // a
        oldOutputLayer = outputLayer.clone();
//        printWeights();
    }
}
