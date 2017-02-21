import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        String mapName = "p11";
        Map map = DataReader.readMapData(mapName);
        int[][] depotCoords = map.getDepotCoords();
        int[][] customerCoords = map.getcustomerCoords();

        Plotter plotter = new Plotter(mapName);
        plotter.addScatterSeries("Depots", depotCoords);
        plotter.addScatterSeries("Customers", customerCoords);

//        Random random = new Random();
//        for (int i = 0; i < 7; i++) {
//            int[][] randomRoute = new int[7][];
//            randomRoute[0] = depotCoords[random.nextInt(depotCoords.length)];
//            for (int j = 1; j < 6; j++) {
//                randomRoute[j] = customerCoords[random.nextInt(customerCoords.length)];
//            }
//            randomRoute[6] = randomRoute[0];
//            plotter.addLineSeries("Route " + Integer.toString(i), randomRoute);
//        }

        plotter.plot();
    }
}


