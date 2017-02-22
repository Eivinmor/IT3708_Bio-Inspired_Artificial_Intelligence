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

    double getEuclideanDistance(boolean aIsDepot, int a, boolean bIsDepot, int b) {
        double[] aCoords;
        if (aIsDepot) aCoords = depotCoords[a];
        else aCoords = customerCoords[a];
        double aX = aCoords[0];
        double aY = aCoords[1];

        double[] bCoords;
        if (bIsDepot) bCoords = depotCoords[b];
        else bCoords = customerCoords[b];
        double bX = bCoords[0];
        double bY = bCoords[1];

        return Math.sqrt(Math.pow(aX - bX, 2) + Math.pow(aY - bY, 2));
    }


}
