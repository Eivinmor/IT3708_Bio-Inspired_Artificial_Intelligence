import java.io.*;
import org.apache.commons.lang3.StringUtils;

public class DataReader {

    private String filePathRoot;

    public DataReader(){
        filePathRoot = System.getProperty("user.dir") + "\\src\\main\\java";
    }

    public void readData(String fileName) throws IOException {
        File dataFile = new File(filePathRoot + "\\Data\\" + fileName);
        BufferedReader reader = new BufferedReader(new FileReader(dataFile));

        // Read meta data
        String[] firstLineArray = StringUtils.split(reader.readLine());
        int maxVehiclesPerDepot = Integer.valueOf(firstLineArray[0]);       // m - maximum number of vehicles per depot
        int numOfCustomers = Integer.valueOf(firstLineArray[1]);            // n - number of customers
        int numOfDepots = Integer.valueOf(firstLineArray[2]);               // t - number of depots

        // Read depot data
        int[][] depotData = new int[numOfDepots][2];
        for (int i = 0; i < numOfDepots; i++) {
            String[] lineArray = StringUtils.split(reader.readLine());
            depotData[i][0] = Integer.valueOf(lineArray[0]);                // D - maximum route duration
            depotData[i][1] = Integer.valueOf(lineArray[1]);                // Q - maximum allowed vehicle load
        }

        // Read customer data
        int[][] customerData = new int[numOfCustomers][5];
        for (int i = 0; i < numOfCustomers; i++) {
            String[] lineArray = StringUtils.split(reader.readLine());
            customerData[i][0] = Integer.valueOf(lineArray[0]);             // i - customer number
            customerData[i][1] = Integer.valueOf(lineArray[1]);             // x - x coordinate
            customerData[i][2] = Integer.valueOf(lineArray[2]);             // y - y coordinate
            customerData[i][3] = Integer.valueOf(lineArray[3]);             // d - necessary service requirement
            customerData[i][4] = Integer.valueOf(lineArray[4]);             // q - demand
        }
    }

    public static void main(String[] args) throws IOException {
        DataReader dataReader = new DataReader();
        dataReader.readData("p01");
    }
}
