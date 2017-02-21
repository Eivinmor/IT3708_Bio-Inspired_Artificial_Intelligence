import java.io.*;

import org.apache.commons.lang3.StringUtils;


public class DataReader {

    public static Map readMapData(String fileName) throws IOException {
        String filePathRoot = System.getProperty("user.dir") + "\\src\\main\\java\\Data";
        File dataFile = new File(filePathRoot + "\\Maps\\" + fileName);
        BufferedReader reader = new BufferedReader(new FileReader(dataFile));

        // Read meta data
        String[] firstLineArray = StringUtils.split(reader.readLine());
        int maxVehiclesPerDepot = Integer.valueOf(firstLineArray[0]);       // m - maximum number of vehicles per depot
        int numOfCustomers = Integer.valueOf(firstLineArray[1]);            // n - number of customers
        int numOfDepots = Integer.valueOf(firstLineArray[2]);               // t - number of depots

        // Read depot data
        double[] depotDuration = new double[numOfDepots];
        double[] depotVehicleMaxLoad = new double[numOfDepots];
        for (int i = 0; i < numOfDepots; i++) {
            String[] lineArray = StringUtils.split(reader.readLine());
            depotDuration[i] = Double.valueOf(lineArray[0]);                // D - maximum route duration
            depotVehicleMaxLoad[i] = Double.valueOf(lineArray[1]);          // Q - maximum allowed vehicle load
        }
        // Read customer data
        double[][] customerCoords = new double[numOfCustomers][2];
        double[] customerDuration = new double[numOfCustomers];
        double[] customerDemand = new double[numOfCustomers];
        for (int i = 0; i < numOfCustomers; i++) {
            String[] lineArray = StringUtils.split(reader.readLine());
            customerCoords[i][0] = Double.valueOf(lineArray[1]);           // x - x coordinate
            customerCoords[i][1] = Double.valueOf(lineArray[2]);           // y - y coordinate
            customerDuration[i] = Double.valueOf(lineArray[3]);            // d - service duration requirement
            customerDemand[i] = Double.valueOf(lineArray[4]);              // q - demand
        }
        // Read depot coordinates
        double[][] depotCoords = new double[numOfDepots][2];
        for (int i = 0; i < numOfDepots; i++) {
            String[] lineArray = StringUtils.split(reader.readLine());
            depotCoords[i][0] = Double.valueOf(lineArray[1]);                // x - x coordinate
            depotCoords[i][1] = Double.valueOf(lineArray[2]);                // y - y coordinate
        }

        Map map = new Map(fileName, maxVehiclesPerDepot, numOfCustomers, numOfDepots,
                depotDuration, depotVehicleMaxLoad, depotCoords,
                customerCoords, customerDuration, customerDemand);
        return map;
    }

//    public void readSolutionData(String fileName) throws IOException {
//        File dataFile = new File(filePathRoot + "\\Solutions\\" + fileName + ".res");
//        BufferedReader reader = new BufferedReader(new FileReader(dataFile));
//        double distance = Double.valueOf(reader.readLine());
//
//        ArrayList<Integer> depotNumbers = new ArrayList<>();
//        ArrayList<Integer> vehicleNumbers = new ArrayList<>();
//        ArrayList<Double> routeDurations = new ArrayList<>();
//        ArrayList<Integer> vehicleLoad = new ArrayList<>();
//        ArrayList<ArrayList<Integer>> solutionSequences = new ArrayList<>();
//
//        String line;
//        while (((line = reader.readLine()) != null) && !("".equals(line))) {
//            String[] lineArray = StringUtils.split(line);
//            depotNumbers.add(Integer.valueOf(lineArray[0]));
//            vehicleNumbers.add(Integer.valueOf(lineArray[1]));
//            routeDurations.add(Double.valueOf(lineArray[2]));
//            vehicleLoad.add(Integer.valueOf(lineArray[3]));
//
//            ArrayList<Integer> routeSequence = new ArrayList<>();
//            for (int j = 4; j < lineArray.length; j++) {
//                routeSequence.add(Integer.valueOf(lineArray[j]));
//            }
//            solutionSequences.add(routeSequence);
//        }
//        Solution solution = new Solution(depotNumbers, vehicleNumbers, routeDurations, vehicleLoad, solutionSequences);
//    }

}
