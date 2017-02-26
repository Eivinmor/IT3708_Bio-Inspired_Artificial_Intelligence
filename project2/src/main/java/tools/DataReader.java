package tools;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

import ga.Solution;
import org.apache.commons.lang3.StringUtils;
import representation.*;

public class DataReader {

    private static String filePathRoot = System.getProperty("user.dir") + "\\src\\main\\resources";

    public static Map readMapData(String fileName) throws IOException {
        File dataFile = new File(filePathRoot + "\\maps\\" + fileName);
        BufferedReader reader = new BufferedReader(new FileReader(dataFile));

        // Read meta data
        String[] firstLineArray = StringUtils.split(reader.readLine());
        int maxVehiclesPerDepot = Integer.valueOf(firstLineArray[0]);           // m - maximum number of vehicles per depot
        int numOfCustomers = Integer.valueOf(firstLineArray[1]);                // n - number of customers
        int numOfDepots = Integer.valueOf(firstLineArray[2]);                   // t - number of depots

        // Read depot data
        double[] depotMaxDuration = new double[numOfDepots];
        double[] depotMaxLoad = new double[numOfDepots];
        for (int i = 0; i < numOfDepots; i++) {
            String[] lineArray = StringUtils.split(reader.readLine());
            depotMaxDuration[i] = Double.valueOf(lineArray[0]);                 // D - maximum route duration
            depotMaxLoad[i] = Double.valueOf(lineArray[1]);                     // Q - maximum allowed vehicle load
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
            double x = Double.valueOf(lineArray[1]);                            // x - x coordinate
            double y = Double.valueOf(lineArray[2]);                            // y - y coordinate
            double maxRouteDuration = depotMaxDuration[i];
            double maxLoadPerVehicle = depotMaxLoad[i];
            depots[i] = new Depot(number, x, y, maxRouteDuration, maxLoadPerVehicle);
        }

        Map map = new Map(fileName, maxVehiclesPerDepot, numOfCustomers, numOfDepots, depots, customers);
        return map;
    }

    public static void writeSolutionToFile(Solution solution) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter((filePathRoot + "solution.txt")));
        writer.write(String.format(Locale.US, "%.2f", solution.getTotalDuration()));
        writer.newLine();

        ArrayList<ArrayList<Unit>>[] solutionRoutes = solution.getRoutes();
        for (int i = 0; i < solutionRoutes.length; i++) {
            for (int j = 0; j < solutionRoutes[i].size(); j++) {
                ArrayList<Unit> route = solutionRoutes[i].get(j);
                double routeDuration = solution.calculateRouteDuration(route);
                double routeLoad = 0;
                StringBuilder routePath = new StringBuilder("0 ");
                for (int k = 0; k < route.size(); k++) {
                    Unit unit = route.get(k);
                    if (unit.getClass().getSimpleName().equals("Customer")) {
                        Customer customer = (Customer)unit;
                        routeLoad += customer.demand;
                        routePath.append(customer.number + " ");
                    }
                }
                routePath.append(" 0");
                System.out.println(String.format(Locale.US, "%d%6d%10.2f%8.0f     %s", i+1, j+1, routeDuration, routeLoad, routePath));
                writer.write(String.format(Locale.US, "%d%6d%10.2f%8.0f     %s", i+1, j+1, routeDuration, routeLoad, routePath));
                writer.newLine();
            }
        }
        writer.close();
    }



}
