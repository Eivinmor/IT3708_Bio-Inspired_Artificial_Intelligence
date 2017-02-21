

public class Map {

    private int maxVehiclesPerDepot, numOfCustomers, numOfDepots;
    private int[][] depotData, customerData;

    public Map(int maxVehiclesPerDepot, int numOfCustomers, int numOfDepots,
               int[][] depotData, int[][] customerData) {
        this.maxVehiclesPerDepot = maxVehiclesPerDepot;
        this.numOfCustomers = numOfCustomers;
        this.numOfDepots = numOfDepots;
        this.depotData = depotData;                     // max route duration, max vehicle load, x, y
        this.customerData = customerData;               // number, x, y, service duration req., demand
    }

    public int[][] getDepotCoords(){
        int[][] depotCoords = new int[numOfDepots][2];
        for (int i = 0; i < numOfDepots; i++) {
            depotCoords[i][0] = depotData[i][2];
            depotCoords[i][1] = depotData[i][3];
        }
        return depotCoords;
    }

    public int[][] getcustomerCoords(){
        int[][] customerCoords = new int[numOfCustomers][2];
        for (int i = 0; i < numOfCustomers; i++) {
            customerCoords[i][0] = customerData[i][1];
            customerCoords[i][1] = customerData[i][2];
        }
        return customerCoords;
    }


}
