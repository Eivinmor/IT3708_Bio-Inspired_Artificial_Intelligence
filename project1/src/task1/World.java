package task1;

import java.util.Random;


public class World {

    private int n, agentCardinalDirection, agentY, agentX;
    private char[][] initialGrid, grid;
    private int[][] cardinalCoordsArray;
    private Random random;
    public boolean simulationEnd;

    public World (){
        random = new Random();
        n = 10;
        initialGrid = generateGrid(n);
        grid = initialGrid.clone();
        simulationEnd = false;
        cardinalCoordsArray  = new int[4][2];
        cardinalCoordsArray[0] = new int[] {-1,0};   // N
        cardinalCoordsArray[1] = new int[] {0,1};    // E
        cardinalCoordsArray[2] = new int[] {1,0};    // S
        cardinalCoordsArray[3] = new int[] {0,-1};   // W
    }

    private char[][] generateGrid(int n){
        char[][] newGrid = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (random.nextBoolean()) newGrid[i][j] = 'F';
                else if (random.nextBoolean()) newGrid[i][j] = 'P';
                else newGrid[i][j] = ' ';
            }
        }
        return newGrid;
    }

    public char observeInDirection(int direction, int distance) {
        int j = cleanCardinalDirection(agentCardinalDirection + direction - 1);
        int obsY = agentY + cardinalCoordsArray[j][0] * (distance + 1);
        int obsX = agentX + cardinalCoordsArray[j][1] * (distance + 1);
        return getSquareStatus(obsY, obsX);
    }

    private char getSquareStatus (int y, int x){
        if (y >= n || y < 0 || x >= n || x < 0)
            return 'W';
        return grid[y][x];
    }

    public void placeAgentRandom(){
        agentCardinalDirection = random.nextInt(4);
        agentY = random.nextInt(n);
        agentX = random.nextInt(n);
        grid[agentY][agentX] = 'A';
    }

    public int moveAgent(int moveDirection){
        agentCardinalDirection = cleanCardinalDirection(agentCardinalDirection + moveDirection - 1);
        int newAgentY = agentY + cardinalCoordsArray[agentCardinalDirection][0];
        int newAgentX = agentX + cardinalCoordsArray[agentCardinalDirection][1];
        int reward = calculateReward(newAgentY, newAgentX);
        if (getSquareStatus(newAgentY, newAgentX) != 'W' && getSquareStatus(agentY, agentX) == 'A') {
            grid[agentY][agentX] = ' ';
            grid[newAgentY][newAgentX] = 'A';
            agentY = newAgentY;
            agentX = newAgentX;
            return reward;
        }
        simulationEnd = true;
        return reward;
    }

    private int calculateReward(int y, int x){
        char squareStatus = getSquareStatus(y, x);
        if (squareStatus == 'F') return 1;
        if (squareStatus == 'P') return -4;
        if (squareStatus == 'W') return -100;
        return 0;
    }

    private int cleanCardinalDirection(int cardinalDirection) {
        int newCardinalDirection = cardinalDirection % 4;
        if (newCardinalDirection < 0) return newCardinalDirection += 4;
        return newCardinalDirection;
    }

    public char[][] getGrid(){
        return grid;
    }
}
