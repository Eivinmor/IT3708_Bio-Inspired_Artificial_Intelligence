package ga;

public class Map {

    private int maxVehiclesPerDepot, numOfCustomers, numOfDepots;
    private double[] depotDuration, depotVehicleMaxLoad, customerDuration, customerDemand;
    private double[][] depotCoords, customerCoords;
    private String name;

    public Map(String name, int maxVehiclesPerDepot, int numOfCustomers, int numOfDepots,
               double[] depotDuration, double[] depotVehicleMaxLoad, double[][] depotCoords,
               double[][] customerCoords, double[] customerDuration, double[] customerDemand) {

        this.name = name;
        this.maxVehiclesPerDepot = maxVehiclesPerDepot;
        this.numOfCustomers = numOfCustomers;
        this.numOfDepots = numOfDepots;

        this.depotCoords = depotCoords;
        this.depotDuration = depotDuration;
        this.depotVehicleMaxLoad = depotVehicleMaxLoad;

        this.customerCoords = customerCoords;
        this.customerDuration = customerDuration;
        this.customerDemand = customerDemand;
    }

    double[][] getDepotCoords(){
        return depotCoords;
    }

    double[][] getCustomerCoords(){
        return customerCoords;
    }

    String getName() {return name;}

}
