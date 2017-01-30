package task1;

import java.util.HashSet;
import java.util.Random;


public class BaselineAgent {

    private World world;
    private Random random;
    private int score;
    private int observeDistance;
    private char[] statusOrder;

    public BaselineAgent(){
        random = new Random();
        score = 0;
        observeDistance = 1;
        statusOrder = new char[] {'F', ' ', 'P'};
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

    public int chooseMoveDirection(char[] observations){
        for (char status : statusOrder) {
            if (observations[1] == status) return 1; // Forward
            if (observations[0] == status) return 0;   // Left
            if (observations[2] == status) return 2;  // Right

//            HashSet<Integer> statusDirectionsSet = new HashSet<>(2);
//            if (observations[0] == status) statusDirectionsSet.add(0);   // Left
//            if (observations[2] == status) statusDirectionsSet.add(2);  // Right
//            if (!statusDirectionsSet.isEmpty()) {
//                int randIndex = random.nextInt(statusDirectionsSet.size());
//                int i = 0;
//                for (int direction : statusDirectionsSet) {
//                    if (i == randIndex) return direction;
//                    i++;
//                }
//            }
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
