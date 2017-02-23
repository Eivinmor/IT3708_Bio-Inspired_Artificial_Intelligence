package representation;
import tools.Formulas;


public class Map {

    public final String name;
    public final int maxVehiclesPerDepot, numOfCustomers, numOfDepots;
    public final Depot[] depots;
    public final Customer[] customers;

    public Map(String name, int maxVehiclesPerDepot, int numOfCustomers, int numOfDepots,
               Depot[] depots, Customer[] customers) {
        this.name = name;
        this.maxVehiclesPerDepot = maxVehiclesPerDepot;
        this.numOfCustomers = numOfCustomers;
        this.numOfDepots = numOfDepots;
        this.depots = depots;
        this.customers = customers;

    }

    public Depot getClosestDepot(Customer customer) {
        Depot closestDepot = depots[0];
        double closestDistance = Double.MAX_VALUE;
        for (Depot depot : depots) {
            double distance = Formulas.euclideanDistance(customer, depot);
            if (distance < closestDistance) {
                closestDepot = depot;
                closestDistance = distance;
            }
        }
        return closestDepot;
    }


}
