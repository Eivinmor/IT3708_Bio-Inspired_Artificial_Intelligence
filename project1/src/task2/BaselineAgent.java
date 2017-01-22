package task2;

import java.util.HashSet;
import java.util.Random;

class BaselineAgent {
    private World world;
    private Random random;
    private int directionIndex; // 0:U, 1:R, 2:D, 3:L
    private int[][] directionCoordsArray;
    private int y, x, score;

    BaselineAgent(World world){
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
    }

    private char[] observe(){
        char[] observedSquares = new char[] {'-', '-', '-', '-'};   // '-' represents no available information
        for (int i = directionIndex - 1; i < directionIndex + 2; i++) {
            int j = cleanDirection(i);
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
        for (char status : new char[] {'F', ' ', 'P'} ) {
            if (observedSquares[directionIndex] == status) return directionIndex;
            HashSet<Integer> statusDirectionsSet = new HashSet<>(3);
            for (int i = 0; i < 4; i++) {
                if (observedSquares[i] == status) statusDirectionsSet.add(i);
            }
            if (!statusDirectionsSet.isEmpty()) {
                int randIndex = random.nextInt(statusDirectionsSet.size());
                int i = 0;
                for (int direction : statusDirectionsSet) {
                    if (i == randIndex) return direction;
                    i++;
                }
            }
        }
        System.out.println("AGENT: No possible move");
        return -1;
    }

    int cleanDirection(int directionIndex) {
        int newDirection = directionIndex % 4;
        if (newDirection < 0) return newDirection += 4;
        return newDirection;
    }

    int getScore(){return score;}

    int getY(){return y;}

    int getX(){return x;}

    void step() {
        char[] observedSquares = observe();
        int chosenMoveDirection = chooseMoveDirection(observedSquares);
        directionIndex = chosenMoveDirection;
        score += move(chosenMoveDirection);
    }
}
