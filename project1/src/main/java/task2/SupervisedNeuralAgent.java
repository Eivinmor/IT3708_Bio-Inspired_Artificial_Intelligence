package task2;

import task1.BaselineAgent;
import java.util.HashMap;
import java.util.Random;


class SupervisedNeuralAgent {

    private task1.World world;
    private Random random;
    private int score, observeDistance, numOfObservedSquares, numOfPossibleSquareStates, numOfPossibleActions;
    private double learningRate;
    private double[][][] weights;
    private HashMap<Character, Integer> inputLayerStatusIndex;
    private task1.BaselineAgent teacher;

    SupervisedNeuralAgent(){
        random = new Random();
        score = 0;
        observeDistance = 1;
        learningRate = 0.01;
        double maxStartWeight = 0.001;
        numOfObservedSquares = 3 * observeDistance;
        numOfPossibleSquareStates = 4;
        numOfPossibleActions = 3;

        weights = new double[numOfObservedSquares][numOfPossibleSquareStates][numOfPossibleActions];
        generateStartWeights(0, maxStartWeight);
        teacher = new BaselineAgent();

        inputLayerStatusIndex = new HashMap<>(4);
        inputLayerStatusIndex.put(' ', 0);
        inputLayerStatusIndex.put('W', 1);
        inputLayerStatusIndex.put('F', 2);
        inputLayerStatusIndex.put('P', 3);
    }

    private char[] observe(){
        char[] observations = new char[3*observeDistance];   // L, F, R
        for (int i = 0; i < observeDistance; i++) {
            for (int j = 0; j < 3; j++) {
                observations[i*3 + j] = world.observeInDirection(j, i);
            }
        }
        return observations; // L, F, R
    }

    private int chooseMoveDirection(double[] neuronOutputs){
        int bestDirection = -1;
        double bestValue = -Double.MAX_VALUE;
        for (int i = 0; i < numOfPossibleActions; i++) {
            if (neuronOutputs[i] > bestValue) {
                bestDirection = i;
                bestValue = neuronOutputs[i];
            }
        }
        return bestDirection;
    }

    private double[] activateNetwork(int[][] inputLayer){
        double[] outputLayer = new double[numOfPossibleActions];
        for (int i = 0; i < inputLayer.length; i++) {
            for (int j = 0; j < inputLayer[i].length; j++) {
                for (int k = 0; k < outputLayer.length; k++) {
                    outputLayer[k] += weights[i][j][k] * inputLayer[i][j];
                }
            }
        }
        return outputLayer;
    }

    // Weights between activated input neurons and all output neurons are changed based on the whether the output was the same as the teacher chose
    private void updateWeights(int[][] neuronInputs, double[] neuronOutputs, int teacherDirection){
        double sumExpOutput = 0;
        for (int i = 0; i < neuronOutputs.length; i++) {
            sumExpOutput += Math.exp(neuronOutputs[i]);
        }
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    int correctChoice = 0;
                    if (teacherDirection == k) correctChoice = 1;
                    weights[i][j][k] += learningRate * deltaRule(neuronOutputs[k], sumExpOutput, correctChoice) * neuronInputs[i][j];
                }
            }
        }
    }

    private double deltaRule(double specificOutput, double sumExpOutput, int correctChoice) {
        double specificExpOutput = Math.exp(specificOutput);
        return - (specificExpOutput/sumExpOutput) + (double)correctChoice;
    }

    private int[][] calculateNeuralInput(char[] observations){
        int[][] neuralInput = new int[numOfObservedSquares][numOfPossibleSquareStates];
        for (int i = 0; i < numOfObservedSquares; i++) {
            int observedStatusIndex = inputLayerStatusIndex.get(observations[i]);   // gets input layer index of status from observation
            neuralInput[i][observedStatusIndex] = 1;
        }
        return neuralInput;
    }

    int getScore(){return score;}

    private void generateStartWeights(double low, double high){
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
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                System.out.format("%6s", "");
                for (int k = 0; k < weights[i][j].length; k++) {
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
        int[][] neuronInputs = calculateNeuralInput(observations);
        double[] neuronOutputs = activateNetwork(neuronInputs);
        int chosenMoveDirection = chooseMoveDirection(neuronOutputs);
        score += world.moveAgent(chosenMoveDirection);
        int teacherDirection = teacher.chooseMoveDirection(observations);
        updateWeights(neuronInputs, neuronOutputs, teacherDirection);
    }
}
