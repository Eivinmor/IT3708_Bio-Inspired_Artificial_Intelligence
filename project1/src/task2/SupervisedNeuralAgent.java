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
    HashMap<Character, Integer> inputLayerStatusIndex;

    SupervisedNeuralAgent(){
        random = new Random();
        score = 0;
        learningRate = 0.01;
        double maxStartWeight = 0.001;

        inputLayer = new int[3][4];
        outputLayer = new double[3];
        weights = new double[3][4][3];
        generateStartWeights(0, maxStartWeight);

        inputLayerStatusIndex = new HashMap(4);
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

    private int move(int moveDirection){
        return world.moveAgent(moveDirection);
    }

    int chooseMoveDirection(char[] observations){
        activateNetwork(observations);
        int bestDirection = -1;
        double bestValue = 0;
        for (int i = 0; i < 3; i++) {
            if (outputLayer[i] > bestValue) {
                bestDirection = i;
                bestValue = outputLayer[i];
            }
        }
        return bestDirection;
    }

    void activateNetwork(char[] observations){
        for (int i = 0; i < 3; i++) {
            int observedStatusIndex = inputLayerStatusIndex.get(observations[i]);   // gets input layer index of status from observaron
            for (int j = 0; j < 4; j++) {
                if (j == observedStatusIndex) inputLayer[i][j] = 1;
                else inputLayer[i][j] = 0;
            }

        }
//        for (int i = 0; i < 3; i++) {
//            int observedStatusIndex = inputLayerStatusIndex.get(observations[i]);   // gets input layer index of status from observaron
//            inputLayer[i][observedStatusIndex] = 1;
//            for (int j = 0; j < 3; j++) {
//                outputLayer[j] += weights[i][observedStatusIndex][j];
//            }
//        }
        for (int j = 0; j < outputLayer.length; j++) {
        }
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

    void updateWeights(int moveDirection, int teacherDirection){
        int correctChoice = 0;
        if (teacherDirection == moveDirection) correctChoice = 1;
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    weights[i][j][k] += learningRate * deltaRule(k, correctChoice) * inputLayer[i][j];
                }
                
            }
        }
    }

    private double deltaRule(int outputNeuron, int correctChoice) {
        double actualExpOutput = outputLayer[outputNeuron];
        double sumExpOutput = 0;
        for (int i = 0; i < 3; i++) {
            sumExpOutput += Math.exp(outputLayer[i]);
        }
        return - (actualExpOutput/sumExpOutput) + correctChoice;
    }

    int getScore(){return score;}

    void registerNewWorld(task1.World newWorld){
        world = newWorld;
        teacher = new BaselineAgent(newWorld);
        score = 0;
    }

    void step() {
        char[] observations = observe();
        int chosenMoveDirection = chooseMoveDirection(observations);
        int teacherDirection = teacher.chooseMoveDirection(observations);
        updateWeights(chosenMoveDirection, teacherDirection);
        score += move(chosenMoveDirection);
    }
}
