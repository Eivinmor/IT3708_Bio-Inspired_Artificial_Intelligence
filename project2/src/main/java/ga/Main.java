package ga;

import representation.*;
import tools.*;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        Map map = DataReader.readMapData("p03");

        for (int i = 0; i < 2; i++) {
            Plotter plotter = new Plotter(map.name);
            plotter.addScatterSeries("Depots", map.depots);
            plotter.addScatterSeries("Customers", map.customers);
            plotter.init();

            Solution solution = new Solution(map);
            solution.generateInitialSolution();
            plotter.plotSolution(solution);
        }

    }

}
