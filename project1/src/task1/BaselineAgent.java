package task1;

import java.util.HashSet;
import java.util.Random;

class BaselineAgent {
    private World world;
    private Random random;
    private int cardinalDirection; // 0:N, 1:E, 2:S, 3:W
    private int[][] cardinalCoordsArray;
    private int y, x, score;

    BaselineAgent(World world){
        this.world = world;
        random = new Random();
        score = 0;
        cardinalCoordsArray  = new int[4][2];
        cardinalCoordsArray[0] = new int[] {-1,0};   // N
        cardinalCoordsArray[1] = new int[] {0,1};    // E
        cardinalCoordsArray[2] = new int[] {1,0};    // S
        cardinalCoordsArray[3] = new int[] {0,-1};   // W
        cardinalDirection = random.nextInt(4);
        y = random.nextInt(world.n);
        x = random.nextInt(world.n);
    }

    private char[] observe(){
        char[] observedSquares = new char[3];   // L, F, R
        for (int i = -1; i < 2; i++) {
            int j = cleanCardinalDirection(cardinalDirection + i);
            int obsY = y + cardinalCoordsArray[j][0];
            int obsX = x + cardinalCoordsArray[j][1];
            observedSquares[i+1] = world.getSquareStatus(obsY, obsX);
        }
        return observedSquares; // L, F, R
    }

    private int move(int moveDirection){
        cardinalDirection = cleanCardinalDirection(cardinalDirection + moveDirection - 1);
        int new_y = y + cardinalCoordsArray[cardinalDirection][0];
        int new_x = x + cardinalCoordsArray[cardinalDirection][1];
        int reward = world.moveAgent(y, x, new_y, new_x);
        y = new_y;
        x = new_x;
        return reward;
    }

    private int chooseMoveDirection(char[] observedSquares){
        for (char status : new char[] {'F', ' ', 'P'} ) {
            if (observedSquares[1] == status) return 1; // Forward
            HashSet<Integer> statusDirectionsSet = new HashSet<>(2);
            if (observedSquares[0] == status) statusDirectionsSet.add(0);   // Left
            else if (observedSquares[2] == status) statusDirectionsSet.add(2);  // Right
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

    private int cleanCardinalDirection(int cardinalDirection) {
        int newCardinalDirection = cardinalDirection % 4;
        if (newCardinalDirection < 0) return newCardinalDirection += 4;
        return newCardinalDirection;
    }

    int getScore(){return score;}

    int getY(){return y;}

    int getX(){return x;}

    void step() {
        char[] observedSquares = observe(); // FIXED
        int chosenMoveDirection = chooseMoveDirection(observedSquares);
        score += move(chosenMoveDirection);
    }
}
