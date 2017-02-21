import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String mapName = "p01";
        Map map = DataReader.readMapData(mapName);
        int[][] depotCoords = map.getDepotCoords();
        int[][] customerCoords = map.getcustomerCoords();

        Plotter plotter = new Plotter(mapName);
        plotter.addDepotsSeries(depotCoords);
        plotter.addCustomersSeries(customerCoords);
        plotter.plot();
    }
}


