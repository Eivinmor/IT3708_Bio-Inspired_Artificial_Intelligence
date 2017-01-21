package task1;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class Agent {
    private World world;
    private Random random;
    private int directionIndex;
    private int[][] directionCoordsArray;
    private int y, x, score;

    Agent(World world){
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
        world.placeAgent(y, x);
    }

    private char[] observe(){
        char[] observedSquares = new char[] {'-', '-', '-', '-'};
        for (int i = directionIndex-1; i < directionIndex + 2; i++) {
            int j = i % 4;
            if (j < 0) j += 4;
            int y = this.y + directionCoordsArray[j][0];
            int x = this.x + directionCoordsArray[j][1];
            observedSquares[j] = world.getSquareStatus(y, x);
        }
        return observedSquares; // {front, right, down, left}
    }

    private int move(int moveDirection){
        int new_y = y + directionCoordsArray[moveDirection][0];
        int new_x = x + directionCoordsArray[moveDirection][1];
        int reward = world.moveAgent(y, x, new_y, new_x);
        y = new_y;
        x = new_x;
        return reward;
    }

    private int chooseMoveDirection(char[] observedSquaresStatusArray){
        for (char status : new char[] {'F', ' ', 'P'} ) {
            if (observedSquaresStatusArray[directionIndex] == status) return directionIndex;
            Set<Integer> dirStatusSet = new HashSet<>(3);
            for (int i = 0; i < 4; i++) {
                if (observedSquaresStatusArray[i] == status && i != directionIndex) dirStatusSet.add(i);
            }
            if (!dirStatusSet.isEmpty()) {
                int randIndex = random.nextInt(dirStatusSet.size());
                int i = 0;
                for (int direction : dirStatusSet) {
                    if (i == randIndex) return direction;
                    i++;
                }
            }
        }
        System.out.println("AGENT No possible move");
        return -1;
    }

    int getScore(){
        return score;
    }

    void step() {
        char[] observedSquares = observe();
        int chosenMoveDir = chooseMoveDirection(observedSquares);
        score += move(chosenMoveDir);
        directionIndex = chosenMoveDir;
    }
}
