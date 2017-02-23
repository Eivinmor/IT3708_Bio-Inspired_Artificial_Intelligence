package representation;
import tools.Formulas;


public class Map {

    public final String name;
    public final int maxVehiclesPerDepot, numOfCustomers, numOfDepots;
    public final Depot[] depots;
    public final Customer[] customers;
    public final double[][] customerDistances, depotCustomerDistances;

    public Map(String name, int maxVehiclesPerDepot, int numOfCustomers, int numOfDepots,
               Depot[] depots, Customer[] customers) {
        this.name = name;
        this.maxVehiclesPerDepot = maxVehiclesPerDepot;
        this.numOfCustomers = numOfCustomers;
        this.numOfDepots = numOfDepots;
        this.depots = depots;
        this.customers = customers;
        this.customerDistances = calculateC2CDistances();
        this.depotCustomerDistances = calculateD2CDistances();

    }

    public Depot getClosestDepot(Customer customer) {
        int closestDepot = 0;
        double closestDistance = Double.MAX_VALUE;
        for (int i = 0; i < numOfDepots; i++) {
            double distance = depotCustomerDistances[i][customer.number -1];
            if (distance < closestDistance) {
                closestDepot = i;
                closestDistance = distance;
            }
        }
        return depots[closestDepot];
    }

    private double[][] calculateC2CDistances() {
        double[][] distances = new double[numOfCustomers][numOfCustomers];
        for (int i = 0; i < numOfCustomers; i++) {
            for (int j = 0; j < numOfCustomers; j++) {
                distances[i][j] = Formulas.euclideanDistance(customers[i], customers[j]);
            }
        }
        return distances;
    }

    private double[][] calculateD2CDistances() {
        double[][] distances = new double[numOfDepots][numOfCustomers];
        for (int i = 0; i < numOfDepots; i++) {
            for (int j = 0; j < numOfCustomers; j++) {
                distances[i][j] = Formulas.euclideanDistance(depots[i], customers[j]);
            }
        }
        return distances;
    }


}
