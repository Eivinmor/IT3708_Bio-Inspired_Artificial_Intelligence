package task2;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.Collector;

class SupervisedNeuralAgent {
    private World world;
    private Random random;
    private int directionIndex; // 0:U, 1:R, 2:D, 3:L
    private int[][] directionCoordsArray;
    private int y, x, score;

    private BaselineAgent teacher;
    private int[][] inputLayer;
    private double[] outputLayer;
    private double[][][] weights;
    HashMap<Character, Integer> inputLaterStatusIndex;

    SupervisedNeuralAgent(World world){
        this.world = world;
        random = new Random();
        score = 0;
        directionCoordsArray  = new int[4][2];
        directionCoordsArray[0] = new int[] {-1,0};   // U
        directionCoordsArray[1] = new int[] {0,1};    // R
        directionCoordsArray[2] = new int[] {1,0};    // D
        directionCoordsArray[3] = new int[] {0,-1};   // L
        directionIndex = random.nextInt(4);
        y = random.nextInt(world.n);
        x = random.nextInt(world.n);

        teacher = new BaselineAgent(world);
        inputLayer = new int[3][4];
        outputLayer = new double[3];
        weights = new double[3][4][3];
        generateStartWeights(0, 0.001);

        inputLaterStatusIndex = new HashMap(4);
        inputLaterStatusIndex.put(' ', 0);
        inputLaterStatusIndex.put('W', 1);
        inputLaterStatusIndex.put('F', 2);
        inputLaterStatusIndex.put('P', 3);
    }

    int getTeacherDirection(char[] observedSquaresStatusArray){
        return teacher.chooseMoveDirection(observedSquaresStatusArray);
    }

    private char[] observe(){
        char[] observedSquares = new char[] {'-', '-', '-', '-'};   // '-' represents no available information
        for (int i = directionIndex-1; i < directionIndex + 2; i++) {
            int j = i % 4;
            if (j < 0) j += 4;
            int obsY = y + directionCoordsArray[j][0];
            int obsX = x + directionCoordsArray[j][1];
            observedSquares[j] = world.getSquareStatus(obsY, obsX);
        }
        return observedSquares; // U, R, D, L
    }

    private int move(int moveDirection){
        int new_y = y + directionCoordsArray[moveDirection][0];
        int new_x = x + directionCoordsArray[moveDirection][1];
        int reward = world.moveAgent(y, x, new_y, new_x);
        y = new_y;
        x = new_x;
        return reward;
    }

    int chooseMoveDirection(char[] observedSquares){
        activateNetwork(observedSquares);
        int bestDirection = -1;
        double bestValue = 0;
        for (int i = 0; i < outputLayer.length; i++) {
            if (outputLayer[i] > bestValue) {
                bestDirection = i;
                bestValue = outputLayer[i];
            }
        }
        return cleanDirection(directionIndex + bestDirection - 1);
    }

    void activateNetwork(char[] observedSquares){
        for (int i = 0; i < 3; i++) {
            int checkDirection = cleanDirection(directionIndex + i - 1);
            int checkedStatusIndex = inputLaterStatusIndex.get(observedSquares[checkDirection]);
            for (int j = 0; j < 3; j++) {
                outputLayer[j] += weights[i][checkedStatusIndex][j];
            }
        }
        for (int j = 0; j < outputLayer.length; j++) {
            System.out.println(outputLayer[j]);
        }
    }

    int cleanDirection(int directionIndex) {
        int newDirection = directionIndex % 4;
        if (newDirection < 0) return newDirection += 4;
        return newDirection;
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

    void updateWeights(int teacherDirection){
        int correctChoice = 0;
        if (teacherDirection == directionIndex) correctChoice = 1;

    }

    int getScore(){return score;}

    int getY(){return y;}

    int getX(){return x;}

    void step() {
        char[] observedSquares = observe();
        int chosenMoveDirection = chooseMoveDirection(observedSquares);
        directionIndex = chosenMoveDirection;
        int teacherDirection = teacher.chooseMoveDirection(observedSquares);
        updateWeights(teacherDirection);
        score += move(chosenMoveDirection);
    }
}
