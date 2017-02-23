package ga;

import representation.*;
import tools.*;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        Map map = DataReader.readMapData("p01");

        Plotter plotter = new Plotter(map.name);
        plotter.plot();

        plotter.addScatterSeries("Depots", map.depots);
        plotter.addScatterSeries("Customers", map.customers);


    }
}
