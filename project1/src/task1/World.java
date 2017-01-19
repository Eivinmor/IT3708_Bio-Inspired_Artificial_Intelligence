package task1;

import java.util.Random;


public class World {

    private char[][] grid;
    private int n;
    private Random random;

    public World (){
        random = new Random();
        n = 10;
        grid = generateGrid(n);
        placeAgent(grid);
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

    private void placeAgent(char[][] grid){
        int x = random.nextInt(n);
        int y = random.nextInt(n);
        grid[y][x] = 'A';
    }

    public void printGrid(char[][] grid){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public char getSquareStatus (int y, int x) {
        if (x >= n || x < 0 || y >= n || y < 0 )
            return 'W';
        return grid[y][x];
    }

    public static void main(String[] args) {
        World world = new World();
        world.printGrid(world.grid);
    }


}
