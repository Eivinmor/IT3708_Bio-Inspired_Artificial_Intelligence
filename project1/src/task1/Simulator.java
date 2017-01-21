package task1;


import java.util.Scanner;
public class Simulator {

    public Simulator(){
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int totalSum = 0;
        for (int i = 0; i < 1000; i++) {
            World world = new World();
            Agent agent = new Agent(world);
//            System.out.println("Initial world:");
//            world.printGrid();
//            System.out.println();
            int iteration = 0;
            while(!world.simulationEnd && iteration < 50) {
//            sc.nextLine();
                agent.step();
//                world.printGrid();
                iteration++;
            }
            int trialSum = agent.getScore();
            System.out.println(trialSum);
            totalSum += trialSum;
        }
        System.out.println("SIMULATION Avg score: " + totalSum/1000);
    }
}
