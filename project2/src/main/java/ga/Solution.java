package ga;

import representation.*;

import java.util.ArrayList;
import java.util.Random;


public class Solution{

    // SETTINGS
    private int clusterProbExponent = -10;

    private Map map;
    private ArrayList<Customer>[] clustering;
    private ArrayList<ArrayList<Unit>>[] routes;
    private Random random;
    private double totalDuration;

    Solution(Map map) {
        this.map = map;
        random = new Random();
        clustering = clusterCustomersToDepots();
        clustering = sortClusterByCustomerDistance(clustering);
        routes = calculateAllRoutes();
        totalDuration = calculateTotalDuration();
    }

    Solution(Solution otherSolution, boolean mutate) {
        this.map = otherSolution.map;
        random = new Random();
        clustering = new ArrayList[map.numOfDepots];
        for (int i = 0; i < map.numOfDepots; i++) {
            this.clustering[i] = new ArrayList<>(otherSolution.clustering[i]);
        }
        if (mutate) mutate();
        routes = calculateAllRoutes();
        totalDuration = calculateTotalDuration();
    }

    private ArrayList<Customer>[] clusterCustomersToDepots() {

        // Set exponential probability to be assigned to depot based on distance
        ArrayList<Customer>[] clusters = new ArrayList[map.numOfDepots];
        for (int i = 0; i < map.numOfDepots; i++) {
            clusters[i] = new ArrayList<>();
        }

        for (Customer customer : map.customers) {
            double[] depotProbArray = new double[map.numOfDepots];

            // Find distances and calculate probabilities to choose depot
            double totalDistanceToAllDepots = 0;
            for (int i = 0; i < map.numOfDepots; i++) {
                totalDistanceToAllDepots += Math.pow(map.getDistance(customer, map.depots[i]), clusterProbExponent);
            }
            for (int i = 0; i < map.numOfDepots; i++) {
                double distance = map.getDistance(customer, map.depots[i]);
                double prob = Math.pow(distance, clusterProbExponent)/totalDistanceToAllDepots;
                depotProbArray[i] = prob;
            }
            // Choose depot
            double randDouble = random.nextDouble();
            for (int i = 0; i < map.numOfDepots; i++) {
                if (depotProbArray[i] > randDouble) {
                    clusters[i].add(customer);
                    break;
                }
                randDouble -= depotProbArray[i];
            }
        }
        return clusters;
    }

    private ArrayList<Customer>[] sortClusterByCustomerDistance(ArrayList<Customer>[] clustering) {
        ArrayList<Customer>[] sortedClustering = new ArrayList[map.numOfDepots];
        for (int i = 0; i < map.numOfDepots; i++) {
            sortedClustering[i] = sortDepotCustomers(map.depots[i], clustering[i]);
        }
        return sortedClustering;
    }

    private ArrayList<Customer> sortDepotCustomers(Depot depot, ArrayList<Customer> depotCustomers) {
        ArrayList<Customer> sortedCustomers = new ArrayList<>();
        ArrayList<Customer> depotCustomersPool = new ArrayList<>(depotCustomers);
        double maxDuration = depot.maxRouteDuration;
        double maxLoad = depot.maxLoadPerVehicle;
        double duration = 0;
        double load = 0;

        Customer firstCustomer =  findClosestCustomer(depot, depotCustomersPool);
        sortedCustomers.add(firstCustomer);
        depotCustomersPool.remove(firstCustomer);

        while (depotCustomersPool.size() > 0) {
            Customer lastCustomer = sortedCustomers.get(sortedCustomers.size()-1);
            Customer closestCustomer = findClosestCustomer(lastCustomer, depotCustomersPool);
            double stepDistance = map.getDistance(lastCustomer, closestCustomer);
            double depotDistance = map.getDistance(closestCustomer, depot);
            double stepService = closestCustomer.serviceDuration;
            double stepDemand = closestCustomer.demand;
            if ((maxDuration > 0 && duration + stepDistance + depotDistance + stepService > maxDuration)
                    || load + stepDemand > maxLoad) {
                // Add while loop with moving towards depot with finding the one with shortest (stepDist + depotDist)
                closestCustomer = findClosestCustomer(depot, depotCustomersPool);
                duration = 0;
                load = 0;
            }
            sortedCustomers.add(closestCustomer);
            duration += stepDistance + stepService;
            load += stepDemand;
            depotCustomersPool.remove(closestCustomer);
        }
        return sortedCustomers;
    }

    private ArrayList<ArrayList<Unit>>[] calculateAllRoutes() {
        ArrayList<ArrayList<Unit>>[] allRoutes = new ArrayList[map.numOfDepots];
        for (int i = 0; i < map.numOfDepots; i++) {
            allRoutes[i] = calculateDepotRoutes(i);
        }
        return allRoutes;
    }

    private ArrayList<ArrayList<Unit>> calculateDepotRoutes(int depotNumber) {
        Depot depot = map.depots[depotNumber];
        ArrayList<ArrayList<Unit>> depotRoutes = new ArrayList<>();
        double maxDuration = depot.maxRouteDuration;
        double maxLoad = depot.maxLoadPerVehicle;
        double duration = 0;
        double load = 0;

        ArrayList<Customer> depotCustomers = clustering[depotNumber];
        ArrayList<Unit> route = new ArrayList<>();
        route.add(depot);

        for (int i = 0; i < depotCustomers.size(); i++) {

            Unit lastUnit = route.get(route.size()-1);
            Customer nextCustomer = depotCustomers.get(i);
            double stepDistance = map.getDistance(lastUnit, nextCustomer);
            double depotDistance = map.getDistance(nextCustomer, depot);
            double stepService = nextCustomer.serviceDuration;
            double stepDemand = nextCustomer.demand;

            if ((maxDuration > 0 && duration + stepDistance + depotDistance + stepService > maxDuration)
                    || load + stepDemand > maxLoad) {
                route.add(depot);
                depotRoutes.add(route);
                route = new ArrayList<>();
                route.add(depot);
                duration = 0;
                load = 0;
            }
            route.add(nextCustomer);
            duration += stepDistance + stepService;
            load += stepDemand;
        }
        route.add(depot);
        depotRoutes.add(route);
        return depotRoutes;
    }

    private Customer findClosestCustomer(Unit unit, ArrayList<Customer> customers) {
        Customer closestCustomer = customers.get(0);
        double shortestDistance = map.getDistance(closestCustomer, unit);

        for (int i = 1; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            double customerDistance = map.getDistance(customer, unit);
            if (customerDistance < shortestDistance) {
                closestCustomer = customer;
                shortestDistance = customerDistance;
            }
        }
        return closestCustomer;
    }

    void mutate() {
        for (int i = 0; i < map.numOfCustomers; i+=50) {
            if (random.nextBoolean()) betweenDepotSwap();
            while (random.nextBoolean()) intraDepotSwap();
        }
    }

    private void intraDepotSwap() {
        int swapDepot = random.nextInt(map.numOfDepots);

        int customer1Index = random.nextInt(clustering[swapDepot].size());
        Customer customer1 = clustering[swapDepot].get(customer1Index);

        int customer2Index = random.nextInt(clustering[swapDepot].size());
        Customer customer2 = clustering[swapDepot].get(customer2Index);

        clustering[swapDepot].set(customer1Index, customer2);
        clustering[swapDepot].set(customer2Index, customer1);
    }

    private void betweenDepotSwap() {
        int swapDepot1 = random.nextInt(map.numOfDepots);
        int swapDepot2 = random.nextInt(map.numOfDepots);
        while (swapDepot1 == swapDepot2) swapDepot2 = random.nextInt(map.numOfDepots);

        int customer1Index = random.nextInt(clustering[swapDepot1].size());
        Customer customer1 = clustering[swapDepot1].get(customer1Index);

        int customer2Index = random.nextInt(clustering[swapDepot2].size());
        Customer customer2 = clustering[swapDepot2].get(customer2Index);

        clustering[swapDepot1].set(customer1Index, customer2);
        clustering[swapDepot2].set(customer2Index, customer1);
    }

    public ArrayList<ArrayList<Unit>>[] getRoutes() {
        return routes;
    }

    private double calculateTotalDuration() {
        if (routes == null)
            routes = calculateAllRoutes();
        double totalDuration = map.totalServiceDuration;
        for (int i = 0; i < routes.length; i++) {
            Depot depot = map.depots[i];
            for (int j = 0; j < routes[i].size(); j++) {
                totalDuration += getRouteDuration(depot, routes[i].get(j));
            }
        }
        return totalDuration;
    }

    double getTotalDuration() {
        return totalDuration;
    }

    private double getRouteDuration(Depot depot, ArrayList<Unit> route) {
        double routeDuration = 0;
        for (int k = 0; k < route.size()-1; k++) {
            routeDuration += map.getDistance(route.get(k), route.get(k+1));
        }
        return routeDuration;
    }

    void printClustering() {
        for (int i = 0; i < clustering.length; i++) {
            for (int j = 0; j < clustering[i].size(); j++) {
                System.out.print(clustering[i].get(j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    void printRoutes() {
        for (int i = 0; i < routes.length; i++) {
            for (int j = 0; j < routes[i].size(); j++) {
                System.out.print(routes[i].get(j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


}

