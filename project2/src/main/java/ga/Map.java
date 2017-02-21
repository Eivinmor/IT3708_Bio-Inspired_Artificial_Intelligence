package ga;

public class Map {

    final int maxVehiclesPerDepot, numOfCustomers, numOfDepots;
    final double[] depotMaxDuration, depotMaxLoad, customerDuration, customerDemand;
    final double[][] depotCoords, customerCoords;
    final String name;

    public Map(String name, int maxVehiclesPerDepot, int numOfCustomers, int numOfDepots,
               double[] depotMaxDuration, double[] depotMaxLoad, double[][] depotCoords,
               double[][] customerCoords, double[] customerDuration, double[] customerDemand) {

        this.name = name;
        this.maxVehiclesPerDepot = maxVehiclesPerDepot;
        this.numOfCustomers = numOfCustomers;
        this.numOfDepots = numOfDepots;

        this.depotCoords = depotCoords;
        this.depotMaxDuration = depotMaxDuration;
        this.depotMaxLoad = depotMaxLoad;

        this.customerCoords = customerCoords;
        this.customerDuration = customerDuration;
        this.customerDemand = customerDemand;
    }

}
