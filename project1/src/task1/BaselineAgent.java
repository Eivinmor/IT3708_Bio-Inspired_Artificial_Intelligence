package task1;

import java.util.HashSet;
import java.util.Random;

class BaselineAgent {
    private World world;
    private Random random;
    private int score;

    BaselineAgent(World world){
        this.world = world;
        random = new Random();
        score = 0;
    }

    private char[] observe(){
        char[] observedSquares = new char[3];   // L, F, R
        for (int i = 0; i < 3; i++) {
            observedSquares[i] = world.observeInDirection(i);
        }
        return observedSquares; // L, F, R
    }

    private int move(int moveDirection){
        return world.moveAgent(moveDirection);
    }

    private int chooseMoveDirection(char[] observedSquares){
        for (char status : new char[] {'F', ' ', 'P'} ) {
            if (observedSquares[1] == status) return 1; // Forward
            HashSet<Integer> statusDirectionsSet = new HashSet<>(2);
            if (observedSquares[0] == status) statusDirectionsSet.add(0);   // Left
            if (observedSquares[2] == status) statusDirectionsSet.add(2);  // Right
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

    int getScore(){return score;}

    void step() {
        char[] observedSquares = observe();
        int chosenMoveDirection = chooseMoveDirection(observedSquares);
        score += move(chosenMoveDirection);
    }
}
