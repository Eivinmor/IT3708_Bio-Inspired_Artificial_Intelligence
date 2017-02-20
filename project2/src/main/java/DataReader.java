import java.io.*;

public class DataReader {

    private String filePathRoot;
    private int maxVehiclesPerDepot, numOfCustomers, numOfDepots;
    private int[][] depotData;

    public DataReader(){
        filePathRoot = System.getProperty("user.dir") + "\\src\\main\\java";
    }

    public void readData(String fileName) throws IOException {
        File dataFile = new File(filePathRoot + "\\Data\\" + fileName);
        BufferedReader reader = new BufferedReader(new FileReader(dataFile));

        String[] firstLineArray = reader.readLine().split(" ");
        maxVehiclesPerDepot = Integer.valueOf(firstLineArray[0]);       // m
        numOfCustomers = Integer.valueOf(firstLineArray[1]);            // n
        numOfDepots = Integer.valueOf(firstLineArray[2]);               // t

        depotData = new int[numOfDepots][2];
        for (int i = 0; i < numOfDepots; i++) {
            String[] lineArray = reader.readLine().split(" ");
            depotData[i][0] = Integer.valueOf(lineArray[0]);            // D
            depotData[i][1] = Integer.valueOf(lineArray[1]);            // Q
        }

    }

    public static void main(String[] args) throws IOException {
        DataReader dataReader = new DataReader();
        dataReader.readData("p01");
    }
}
