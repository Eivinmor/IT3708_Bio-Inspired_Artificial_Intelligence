package ga;

import representation.*;
import tools.*;
import java.io.IOException;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) throws IOException {
        Map map = DataReader.readMapData("p01");

        Plotter plotter = new Plotter(map.name);
        plotter.plot();

        plotter.addScatterSeries("Depots", map.depots);
        plotter.addScatterSeries("Customers", map.customers);


        Solution solution = new Solution(map);
        solution.generateRandomSolution();
        int key = 0;
        for (SolutionDepot solutionDepot : solution.getSolutionDepots()) {
            for (SolutionRoute solutionRoute : solutionDepot.getSolutionRoutes()) {
                ArrayList<Unit> route = new ArrayList<>();
                route.add(solutionDepot);
                for (Customer customer : solutionRoute.getCustomers()) {
                    route.add(customer);
                }
                route.add(solutionDepot);
                plotter.addLineSeries(Integer.toString(key), route);
                key++;
            }

        }

    }
}
