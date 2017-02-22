package representation;

public class Map {

    public final int maxVehiclesPerDepot, numOfCustomers, numOfDepots;
    public final Depot[] depots;
    public final Customer[] customers;
    public final String name;

    public Map(String name, int maxVehiclesPerDepot, int numOfCustomers, int numOfDepots,
               Depot[] depots, Customer[] customers) {
        this.name = name;
        this.maxVehiclesPerDepot = maxVehiclesPerDepot;
        this.numOfCustomers = numOfCustomers;
        this.numOfDepots = numOfDepots;
        this.depots = depots;
        this.customers = customers;
    }

    double euclideanDistance(Unit a, Unit b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getX() - b.getY(), 2));
    }

    public int getClosestDepot(int customer) {
        int closestDepot = -1;
        double closestDistance = Double.MAX_VALUE;
        for (int i = 0; i < numOfDepots; i++) {
            double distance = euclideanDistance(customers[customer], depots[i]);
            if (distance < closestDistance) {
                closestDepot = i;
                closestDistance = distance;
            }
        }
        return closestDepot;
    }


}
