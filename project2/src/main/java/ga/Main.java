package ga;

import representation.*;
import tools.*;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Map map = DataReader.readMapData("p01");

        Solution solution = new Solution(map);
        solution.generateRandomSolution();

        Plotter plotter = new Plotter(map.name);
        plotter.plot();

        plotter.addScatterSeries("Depots", map.depots);
        plotter.addScatterSeries("Customers", map.customers);


    }
}
