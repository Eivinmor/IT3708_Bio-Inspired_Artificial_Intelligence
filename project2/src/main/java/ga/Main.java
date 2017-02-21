package ga;

import tools.DataReader;
import tools.Plotter;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Map map = DataReader.readMapData("p01");
        double[][] depotCoords = map.getDepotCoords();
        double[][] customerCoords = map.getCustomerCoords();

        Plotter plotter = new Plotter(map.getName());
        plotter.plot();
        plotter.addScatterSeries("Depots", depotCoords);
        plotter.addScatterSeries("Customers", customerCoords);

        Random random = new Random();
        for (int k = 0; k < 10; k++) {
            for (int i = 0; i < 7; i++) {
                TimeUnit.SECONDS.sleep(1);
                double[][] randomRoute = new double[7][];
                randomRoute[0] = depotCoords[random.nextInt(depotCoords.length)];
                for (int j = 1; j < 6; j++) {
                    randomRoute[j] = customerCoords[random.nextInt(customerCoords.length)];
                }
                randomRoute[6] = randomRoute[0];
                plotter.addLineSeries("Route " + Integer.toString(i), randomRoute);
            }
            plotter.clearLineSeries();
        }
    }
}
