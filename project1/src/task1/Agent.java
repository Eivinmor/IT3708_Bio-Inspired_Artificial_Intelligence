package task1;
import java.util.Random;


class Agent {
    private World world;
    private Random random;
    private int direction;
    private char[] directionArray;
    private int y, x;

    Agent(World world){
        this.world = world;
        random = new Random();
        directionArray = new char[] {'U', 'R', 'D', 'L'};
        direction = getRandomDirection();
        y = random.nextInt(world.n);
        x = random.nextInt(world.n);
        observe();
    }

    private char[] observe(){
        char[] observedSquares = new char[4];
        return observedSquares;
    }

    private int getRandomDirection(){
        return random.nextInt(4);
    }

    private void move(char newDirection){

    }
}
