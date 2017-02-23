package ga;

import representation.*;
import tools.*;
import java.io.IOException;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) throws IOException {
        Map map = DataReader.readMapData("p10");

        Plotter plotter = new Plotter(map.name);
        plotter.addScatterSeries("Depots", map.depots);
        plotter.addScatterSeries("Customers", map.customers);
        plotter.plot();

        Solution solution = new Solution(map);
        solution.generateInitialSolution();
        ArrayList<Unit>[] routes = solution.getRoutes();
        for (int i = 0; i < routes.length; i++) {
            ArrayList<Unit> route = new ArrayList<>();
            route.add(routes[i].get(0));
            int j;
            for (j = 1; j < routes[i].size(); j++) {
                Unit unit = routes[i].get(j);
                if (unit.getClass().getSimpleName().equals("Depot")) {
                    route.add(unit);
                    plotter.addLineSeries(i+1 + ":" + j+1, route);
                    route = new ArrayList<>();
                }
                route.add(unit);
            }
            plotter.addLineSeries(i+1 + ":" + j+1, route);
        }
    }
}
