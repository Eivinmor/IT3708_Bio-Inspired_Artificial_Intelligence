package task1;


import java.util.Scanner;
public class Simulator {

    public Simulator(){
    }

    public static void main(String[] args) {
        World world = new World();
        Agent agent = new Agent(world);
        world.printGrid();
        Scanner sc = new Scanner(System.in);
        while(!world.simulationEnd) {
            agent.step();
            world.printGrid();
            sc.nextLine();
        }
    }
}
