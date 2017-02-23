package ga;

import representation.*;

import java.util.ArrayList;


public class Solution implements Comparable<Solution> {

    private double totalDistance, weightedScore;
    private Map map;
    private ArrayList<Customer>[] depotCustomersArray;
    private ArrayList<Unit>[] depotRoutesArray;

    Solution(Map map){
        this.map = map;
        depotCustomersArray = new ArrayList[4];
        depotRoutesArray = new ArrayList[4];
        for (int i = 0; i < map.numOfDepots; i++) {
            depotCustomersArray[i] = new ArrayList<>();
            depotRoutesArray[i] = new ArrayList<>();
        }
    }

    public int compareTo(Solution o) {
        return 0;
        // Weighted score basert på distance og antall kjøretøy
    }

    void generateInitialSolution() {
        clustering();
        routingAndScheduling();
    }

    void clustering() {
        for (Customer customer : map.customers) {
            depotCustomersArray[map.getClosestDepot(customer).number-1].add(customer);
        }
    }

    void routingAndScheduling() {

        for (int i = 0; i < depotCustomersArray.length; i++) {
            Depot depot = map.depots[i];
            ArrayList<Customer> depotCustomersPool = new ArrayList<>(depotCustomersArray[i]);
            ArrayList<Unit> depotRoutes = new ArrayList<>();
            depotRoutes.add(depot);

            double maxDuration = depot.maxRouteDuration;
            double maxLoad = depot.maxLoadPerVehicle;
            double duration = 0;
            double load = 0;

            for (int j = 0; j < depotCustomersArray[i].size(); j++) {
                Customer closestCustomer = depotCustomersPool.get(0);
                Unit lastUnit = depotRoutes.get(depotRoutes.size()-1);
                double shortestDistance = map.getDistance(closestCustomer, lastUnit);
                for (int k = 1; k < depotCustomersPool.size(); k++) {
                    Customer customer = depotCustomersPool.get(k);
                    double customerDistance = map.getDistance(customer, lastUnit);
                    if (customerDistance < shortestDistance) {
                        closestCustomer = customer;
                        shortestDistance = customerDistance;
                    }
                }
                depotCustomersPool.remove(closestCustomer);

                if ( load + closestCustomer.demand > maxLoad || (maxDuration > 0 && duration + map.getDistance(depot, closestCustomer) > maxDuration)) {
                    depotRoutes.add(depot);
                    duration = 0;
                    load = 0;
                }
                depotRoutes.add(closestCustomer);
                load += closestCustomer.demand;
                duration += closestCustomer.serviceDuration;
            }
            depotRoutes.add(depot);
            depotRoutesArray[i] = depotRoutes;
        }
    }

    ArrayList<Unit>[] getRoutes() {
        return depotRoutesArray;
    }

}
