package task1;

import java.util.HashSet;
import java.util.Random;


public class BaselineAgent {

    private World world;
    private Random random;
    private int score;

    public BaselineAgent(){
        random = new Random();
        score = 0;
    }

    private char[] observe(){
        char[] observations = new char[3];   // L, F, R
        for (int i = 0; i < 3; i++) {
            observations[i] = world.observeInDirection(i);
        }
        return observations; // L, F, R
    }

    public int chooseMoveDirection(char[] observations){
        for (char status : new char[] {'F', ' ', 'P'} ) {
            if (observations[1] == status) return 1; // Forward
            HashSet<Integer> statusDirectionsSet = new HashSet<>(2);
            if (observations[0] == status) statusDirectionsSet.add(0);   // Left
            if (observations[2] == status) statusDirectionsSet.add(2);  // Right
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

    public void registerNewWorld(task1.World newWorld){
        world = newWorld;
        score = 0;
    }

    int getScore(){return score;}

    void step() {
        char[] observations = observe();
        int chosenMoveDirection = chooseMoveDirection(observations);
        score += world.moveAgent(chosenMoveDirection);
    }
}