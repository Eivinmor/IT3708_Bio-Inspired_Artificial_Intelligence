package task4;

import java.util.HashMap;
import java.util.Random;


class ExtendedReinforcedNeuralAgent {

    private task1.World world;
    private Random random;
    private int score, observeDistance, numOfObservedSquares, numOfPossibleSquareStates, numOfPossibleActions;
    private double learningRate, discountFactor;
    private double[][][] weights;
    private HashMap<Character, Integer> inputLayerStatusIndex;


    ExtendedReinforcedNeuralAgent(){
        random = new Random();
        score = 0;
        observeDistance = 3;
        learningRate = 0.01;
        discountFactor = 0.9;
        double maxStartWeight = 0.001;
        numOfObservedSquares = 3 * observeDistance;
        numOfPossibleSquareStates = 4;
        numOfPossibleActions = 3;

        weights = new double[numOfObservedSquares][numOfPossibleSquareStates][numOfPossibleActions];
        generateStartWeights(0, maxStartWeight);

        inputLayerStatusIndex = new HashMap<>(4);
        inputLayerStatusIndex.put(' ', 0);
        inputLayerStatusIndex.put('W', 1);
        inputLayerStatusIndex.put('F', 2);
        inputLayerStatusIndex.put('P', 3);
    }

    private char[] observe(){
        char[] observations = new char[3*observeDistance];   // L, L, L, F, F, F, R, R, R
        for (int i = 0; i < observeDistance; i++) {
            for (int j = 0; j < 3; j++) {
                observations[i*3 + j] = world.observeInDirection(j, i);
            }
        }
        return observations; // L, L, L, F, F, F, R, R, R
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

    // Weights between activated input neurons and the best output neuron are changed based on the output value of the next iteration
    private void updateWeights(int reward, int[][] neuronInputs, double[] neuronOutputs, int chosenMoveDirection, double nextMaxOutputValue){
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j][chosenMoveDirection] += learningRate * deltaRule(reward, neuronOutputs[chosenMoveDirection], nextMaxOutputValue) * neuronInputs[i][j];
            }
        }
    }

    private double deltaRule(int reward, double output, double nextMaxOutputValue) {
        return (reward + discountFactor * nextMaxOutputValue - output);
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
        int reward = world.moveAgent(chosenMoveDirection);
        score += reward;

        char[] nextObservations = observe();
        int[][] nextNeuronInputs = calculateNeuralInput(nextObservations);
        double[] nextNeuronOutputs = activateNetwork(nextNeuronInputs);
        int nextChosenMoveDirection = chooseMoveDirection(nextNeuronOutputs);
        double nextMaxOutputValue = nextNeuronOutputs[nextChosenMoveDirection];

        updateWeights(reward, neuronInputs, neuronOutputs, chosenMoveDirection, nextMaxOutputValue);
    }
}
