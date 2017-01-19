package task1;

import java.util.Random;


class World {

    int n;
    private char[][] initialGrid, grid;
    private Random random;

    World (){
        random = new Random();
        n = 10;
        initialGrid = generateGrid(n);
        placeAgent(initialGrid);
        grid = initialGrid.clone();
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

    public char[][] getGrid(){
        return grid;
    }

    private void placeAgent(char[][] grid){
        int y = random.nextInt(n);
        int x = random.nextInt(n);
        grid[y][x] = 'A';
    }
}
