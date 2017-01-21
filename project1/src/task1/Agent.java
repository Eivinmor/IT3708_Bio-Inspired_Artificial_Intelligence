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
    private String[] directionNameArray;

    Agent(World world){
        this.world = world;
        random = new Random();
        score = 0;
        directionCoordsArray  = new int[4][2];
        directionCoordsArray[0] = new int[] {-1,0};   // U
        directionCoordsArray[1] = new int[] {0,1};    // R
        directionCoordsArray[2] = new int[] {1,0};    // D
        directionCoordsArray[3] = new int[] {0,-1};   // L
        directionNameArray = new String[] {"UP", "RIGHT", "DOWN", "LEFT"};
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
//            System.out.println(j);
            int y = this.y + directionCoordsArray[j][0];
            int x = this.x + directionCoordsArray[j][1];
//            System.out.println(y + "," + x);
            observedSquares[j] = world.getSquareStatus(y, x);
//            System.out.println(observedSquares[j]);
//            System.out.println("-----");
        }
//        for (int i = 0; i < observedSquares.length; i++) {
//            System.out.print(observedSquares[i] + ", ");
//        }
        return observedSquares; // {front, right, down, left}
    }

    private int move(int moveDirection){
        int new_y = y + directionCoordsArray[moveDirection][0];
        int new_x = x + directionCoordsArray[moveDirection][1];
//        System.out.println("AGENT Old: " + y + "," + x);
//        System.out.println("AGENT New: " + new_y + "," + new_x);
        int reward = world.moveAgent(y, x, new_y, new_x);
        y = new_y;
        x = new_x;
        return reward;
    }

    private int chooseMoveDirection(char[] candidateSquareStatuses){
        for (char status : new char[] {'F', ' ', 'P'} ) {
//            System.out.println("AGENT Checking status :" + status);
            if (candidateSquareStatuses[directionIndex] == status) return directionIndex;
            Set<Integer> dirStatusSet = new HashSet<>(3);
            for (int i = 0; i < 4; i++) {
                if (candidateSquareStatuses[i] == status && i != directionIndex) dirStatusSet.add(i);
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
//        System.out.println("AGENT location: "  + y + "," + x);
//        System.out.println("AGENT Current direction: " + directionNameArray[directionIndex]);
        char[] observedSquares = observe();
        int chosenMoveDir = chooseMoveDirection(observedSquares);
//        System.out.println("AGENT Moving in direction: " + directionNameArray[chosenMoveDir]);
        score += move(chosenMoveDir);
        directionIndex = chosenMoveDir;
//        System.out.println("AGENT Score: " + score);
    }
}
