package task1;


public class Simulator {

    public Simulator(){
    }

    public static void main(String[] args) {
        World world = new World();
        Agent agent = new Agent(world);
        world.printGrid();
    }
}
