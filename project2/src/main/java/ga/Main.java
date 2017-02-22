package ga;

import representation.Map;
import tools.DataReader;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Map map = DataReader.readMapData("p01");
        double[][] depotCoords = map.depotCoords;
        double[][] customerCoords = map.customerCoords;

        Solution solution = new Solution(map);
        solution.generateRandomSolution();

//        Plotter plotter = new Plotter(map.name);
//        plotter.plot();
//        plotter.addScatterSeries("Depots", depotCoords);
//        plotter.addScatterSeries("Customers", customerCoords);


    }
}
