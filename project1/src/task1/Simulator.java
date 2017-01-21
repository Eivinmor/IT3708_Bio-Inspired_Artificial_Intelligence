package task1;


import java.util.Scanner;
public class Simulator {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int totalSum = 0;
//        for (int i = 0; i < 1000; i++) {

        World world = new World();
        System.out.println("Initial world:");
        world.printGrid();
        Agent agent = new Agent(world);
        System.out.println();
        int iteration = 0;
        while(!world.simulationEnd && iteration < 50) {
            sc.nextLine();
            agent.step();
            world.printGrid();
            iteration++;
        }
        System.out.println("Score: " + agent.getScore());

//            totalSum += trialSum;
//        }
//        System.out.println("SIMULATION Avg score: " + totalSum/1000);
    }
}
