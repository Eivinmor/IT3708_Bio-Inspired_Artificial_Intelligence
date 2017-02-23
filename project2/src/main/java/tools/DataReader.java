package tools;

import java.io.*;
import org.apache.commons.lang3.StringUtils;
import representation.*;

public class DataReader {

    public static Map readMapData(String fileName) throws IOException {
        String filePathRoot = System.getProperty("user.dir") + "\\src\\main\\java\\data";
        File dataFile = new File(filePathRoot + "\\maps\\" + fileName);
        BufferedReader reader = new BufferedReader(new FileReader(dataFile));

        // Read meta data
        String[] firstLineArray = StringUtils.split(reader.readLine());
        int maxVehiclesPerDepot = Integer.valueOf(firstLineArray[0]);       // m - maximum number of vehicles per depot
        int numOfCustomers = Integer.valueOf(firstLineArray[1]);            // n - number of customers
        int numOfDepots = Integer.valueOf(firstLineArray[2]);               // t - number of depots

        // Read depot data
        double[] depotMaxDuration = new double[numOfDepots];
        double[] depotMaxLoad = new double[numOfDepots];
        for (int i = 0; i < numOfDepots; i++) {
            String[] lineArray = StringUtils.split(reader.readLine());
            depotMaxDuration[i] = Double.valueOf(lineArray[0]);                // D - maximum route duration
            depotMaxLoad[i] = Double.valueOf(lineArray[1]);          // Q - maximum allowed vehicle load
        }
        // Read customer data and create Customers
        Customer[] customers = new Customer[numOfCustomers];
        for (int i = 0; i < numOfCustomers; i++) {
            String[] lineArray = StringUtils.split(reader.readLine());
            int number = Integer.valueOf(lineArray[0]);
            double x = Double.valueOf(lineArray[1]);                            // x - x coordinate
            double y = Double.valueOf(lineArray[2]);                            // y - y coordinate
            double serviceDuration = Double.valueOf(lineArray[3]);              // d - service duration requirement
            double demand = Double.valueOf(lineArray[4]);                       // q - demand
            customers[i] = new Customer(number, x, y, serviceDuration, demand);
        }
        // Read depot coordinates and create Depots
        Depot[] depots = new Depot[numOfDepots];
        for (int i = 0; i < numOfDepots; i++) {
            String[] lineArray = StringUtils.split(reader.readLine());
            int number = Integer.valueOf(i+1);
            double x = Double.valueOf(lineArray[1]);           // x - x coordinate
            double y = Double.valueOf(lineArray[2]);           // y - y coordinate
            double maxRouteDuration = depotMaxDuration[i];
            double maxLoadPerVehicle = depotMaxLoad[i];
            depots[i] = new Depot(number, x, y, maxRouteDuration, maxLoadPerVehicle);
        }

        Map map = new Map(fileName, maxVehiclesPerDepot, numOfCustomers, numOfDepots, depots, customers);
        return map;
    }


}
