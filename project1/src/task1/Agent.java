package task1;
import java.util.HashSet;
import java.util.Random;
import java.util.HashMap;
import java.util.Set;

class Agent {
    private World world;
    private Random random;
    private int directionIndex;
    private int[][] directionArray;
    private HashMap directionDict;
    private int y, x;

    Agent(World world){
        this.world = world;
        random = new Random();
        directionArray  = new int[4][2];
        directionArray[0] = new int[] {-1,0};   // U
        directionArray[1] = new int[] {0,1};    // R
        directionArray[2] = new int[] {1,0};    // D
        directionArray[3] = new int[] {0,-1};   // L
        directionIndex = random.nextInt(4);
        y = random.nextInt(world.n);
        x = random.nextInt(world.n);
        world.placeAgent(y, x);
    }

    public char[] observe(){
        char[] observedSquares = new char[] {'-', '-', '-', '-'};
        for (int i = directionIndex-1; i < directionIndex + 2; i++) {
            int j = i % 4;
            if (j < 0) j += 4;
            System.out.println(y + "," + x);
            System.out.println(j);
            int y = this.y + directionArray[j][0];
            int x = this.x + directionArray[j][1];
            System.out.println(y + "," + x);
            observedSquares[j] = world.getSquareStatus(y, x);
            System.out.println(observedSquares[j]);
            System.out.println("-----");
        }
        for (int i = 0; i < observedSquares.length; i++) {
        }
        return observedSquares;
    }

    private void move(int moveDirection){
        int new_y = y + directionArray[moveDirection][0];
        int new_x = x + directionArray[moveDirection][1];
        world.moveAgent(y, x, new_y, new_x);
        y = new_y;
        x = new_x;
    }

    private int chooseMoveDirection(char[] candidateSquareStatuses){
        for (char status : new char[] {'F', ' ', 'P'} ) {
            if (candidateSquareStatuses[0] == status) return 0;
            Set<Integer> dirStatusSet = new HashSet<>(3);
            for (int i = 1; i < 4; i++) {
                if (candidateSquareStatuses[i] == status) dirStatusSet.add(i);
            }
            if (!dirStatusSet.isEmpty()) {
                int randIndex = random.nextInt(dirStatusSet.size());
                int i = 0;
                for (int direction : dirStatusSet) {
                    if (i == randIndex) return randIndex;
                i++;
                }
            }
        }
        return 0;
    }

    void step() {
        char[] observedSquares = observe();
        int chosenMoveDir = chooseMoveDirection(observedSquares);
        move(chosenMoveDir);
    }
}
