package task1;

import java.util.Random;


class World {

    int n;
    private char[][] initialGrid, grid;
    private Random random;
    public boolean simulationEnd;

    World (){
        random = new Random();
        n = 10;
        initialGrid = generateGrid(n);
        grid = initialGrid.clone();
        simulationEnd = false;
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

    void printGrid(){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public char getSquareStatus (int y, int x){
        if (x >= n || x < 0 || y >= n || y < 0 )
            return 'W';
        return grid[y][x];
    }

    void placeAgent(int y, int x){
        grid[y][x] = 'A';
    }

    int moveAgent(int old_y, int old_x, int new_y, int new_x){
        if (getSquareStatus(new_y, new_x) != 'W' && getSquareStatus(old_y, old_x) == 'A') {
            int reward = calculateReward(new_y, new_x);
            grid[old_y][old_x] = ' ';
            grid[new_y][new_x] = 'A';
            return reward;
        }
        System.out.println("Invalid move");
        simulationEnd = true;
        return 0;
    }

    private int calculateReward(int y, int x){
        char squareStatus = getSquareStatus(y, x);
        if (squareStatus == 'F') return 1;
        if (squareStatus == 'P') return -4;
        if (squareStatus == 'W') {
            simulationEnd = true;
            return -100;
        }
        return 0;
    }
}
