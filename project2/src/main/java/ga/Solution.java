package ga;

import representation.*;

import java.util.ArrayList;
import java.util.Random;


public class Solution{

    // SETTINGS
    private int clusterProbExponent = -10;

    private double durationCostWeight = 1;
    private double numOfVehiclesCostWeight = 100;
    private double overVehicleLimitCostWeight = 10000;

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
        for (int i = 0; i < 1; i++) {
            double randDouble = random.nextDouble();
            if (randDouble > 0.8) betweenDepotSwap();
            else if (randDouble > 0.6) reverseMutation();
//            else if (randDouble > 0.4)
//                singleCustomerOptimalBetweenDepotReposition();
            else
                singleCustomerOptimalIntraDepotReposition();
        }
    }

    private void singleCustomerOptimalIntraDepotReposition() {
        ArrayList<ArrayList<Unit>>[] tempRoutes = calculateAllRoutes();
        int depotIndex = random.nextInt(map.numOfDepots);
        int routeIndex = random.nextInt(tempRoutes[depotIndex].size());

        // TODO Denne sjekken kan kanskje gjøres smartere
        if (tempRoutes[depotIndex].get(routeIndex).size() > 2) {
            int customerIndex = random.nextInt(tempRoutes[depotIndex].get(routeIndex).size()-2)+1; // Avoid depots
            Customer customer = (Customer)tempRoutes[depotIndex].get(routeIndex).get(customerIndex);
            tempRoutes[depotIndex].get(routeIndex).remove(customer);

            int bestInsertionRouteIndex = 0;
            int bestInsertionCustomerIndex = 1;
            double bestInsertionCost = Double.MAX_VALUE;
            for (int j = 0; j < tempRoutes[depotIndex].size(); j++) {  // Route
                for (int k = 1; k < tempRoutes[depotIndex].get(j).size(); k++) {  // Customer
                    double insertionCost = getInsertionCost(customer, tempRoutes[depotIndex].get(j).get(k-1), tempRoutes[depotIndex].get(j).get(k));
                    if (insertionCost < bestInsertionCost) {
                        // TODO Check if it ends up in this route or the next
                        bestInsertionCost = insertionCost;
                        bestInsertionRouteIndex = j;
                        bestInsertionCustomerIndex = k;
                    }
                }
            }
            ArrayList<Unit> chosenRoute = tempRoutes[depotIndex].get(bestInsertionRouteIndex);
            ArrayList<Unit> newRoute = new ArrayList<>();
            newRoute.addAll(chosenRoute.subList(0, bestInsertionCustomerIndex));
            newRoute.add(customer);
            newRoute.addAll(chosenRoute.subList(bestInsertionCustomerIndex, chosenRoute.size()));
            tempRoutes[depotIndex].set(bestInsertionRouteIndex, newRoute);
            clustering = convertRoutesToClustering(tempRoutes);
        }
    }

    private void singleCustomerOptimalBetweenDepotReposition() {
        ArrayList<ArrayList<Unit>>[] tempRoutes = calculateAllRoutes();
        int depotIndex = random.nextInt(map.numOfDepots);
        int routeIndex = random.nextInt(tempRoutes[depotIndex].size());

        // TODO Denne sjekken kan kanskje gjøres smartere
        if (tempRoutes[depotIndex].get(routeIndex).size() > 2) {
            int customerIndex = random.nextInt(tempRoutes[depotIndex].get(routeIndex).size() - 2) + 1; // Avoid depots
            Customer customer = (Customer) tempRoutes[depotIndex].get(routeIndex).get(customerIndex);
            tempRoutes[depotIndex].get(routeIndex).remove(customer);

            int bestInsertionDepotIndex = 0;
            int bestInsertionRouteIndex = 0;
            int bestInsertionCustomerIndex = 1;
            double bestInsertionCost = Double.MAX_VALUE;
            for (int i = 0; i < tempRoutes.length; i++) {  // Depot
                for (int j = 0; j < tempRoutes[i].size(); j++) {  // Route
                    for (int k = 1; k < tempRoutes[i].get(j).size(); k++) {  // Customer
                        double insertionCost = getInsertionCost(customer, tempRoutes[i].get(j).get(k - 1), tempRoutes[i].get(j).get(k));
                        if (insertionCost < bestInsertionCost) {
                            // TODO Check if it ends up in this route or the next
                            bestInsertionCost = insertionCost;
                            bestInsertionDepotIndex = i;
                            bestInsertionRouteIndex = j;
                            bestInsertionCustomerIndex = k;
                        }
                    }
                }
            }
            ArrayList<Unit> chosenRoute = tempRoutes[bestInsertionDepotIndex].get(bestInsertionRouteIndex);
            ArrayList<Unit> newRoute = new ArrayList<>();
            newRoute.addAll(chosenRoute.subList(0, bestInsertionCustomerIndex));
            newRoute.add(customer);
            newRoute.addAll(chosenRoute.subList(bestInsertionCustomerIndex, chosenRoute.size()));
            tempRoutes[bestInsertionDepotIndex].set(bestInsertionRouteIndex, newRoute);
            clustering = convertRoutesToClustering(tempRoutes);
        }
    }

    private double getInsertionCost (Customer insertCustomer, Unit preUnit, Unit postUnit) {
        return map.getDistance(preUnit, insertCustomer)
                + map.getDistance(insertCustomer, postUnit)
                - map.getDistance(postUnit, preUnit);
    }

    private void reverseMutation() {
//        printClustering();
        int depotIndex = random.nextInt(map.numOfDepots);
        int cutPoint1 = random.nextInt(clustering[depotIndex].size()-1);
        int cutPoint2 = random.nextInt(clustering[depotIndex].size() - cutPoint1) + cutPoint1;
        ArrayList<Customer> reverse = new ArrayList<>(clustering[depotIndex].subList(cutPoint1, cutPoint2));
        for (int i = 0; i < reverse.size(); i++) {
            clustering[depotIndex].set(cutPoint1 + i, reverse.get(reverse.size()-1-i));
        }
//        System.out.println(depotIndex + ", " + cutPoint1 + ", " + cutPoint2);
//        printClustering();
    }

    private void intraDepotSwap() {
        int depotIndex = random.nextInt(map.numOfDepots);

        int customer1Index = random.nextInt(clustering[depotIndex].size());
        Customer customer1 = clustering[depotIndex].get(customer1Index);

        int customer2Index = random.nextInt(clustering[depotIndex].size());
        Customer customer2 = clustering[depotIndex].get(customer2Index);

        clustering[depotIndex].set(customer1Index, customer2);
        clustering[depotIndex].set(customer2Index, customer1);
    }

    private void betweenDepotSwap() {
        int depot1Index = random.nextInt(map.numOfDepots);
        int depot2Index = random.nextInt(map.numOfDepots);
        while (depot1Index == depot2Index) depot2Index = random.nextInt(map.numOfDepots);

        int customer1Index = random.nextInt(clustering[depot1Index].size());
        Customer customer1 = clustering[depot1Index].get(customer1Index);

        int customer2Index = random.nextInt(clustering[depot2Index].size());
        Customer customer2 = clustering[depot2Index].get(customer2Index);

        clustering[depot1Index].set(customer1Index, customer2);
        clustering[depot2Index].set(customer2Index, customer1);
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
                totalDuration += calculateRouteDuration(depot, routes[i].get(j));
            }
        }
        return totalDuration;
    }

    double getTotalDuration() {
        return totalDuration;
    }

    double getCost() {
        double cost = 0;
        cost += getTotalDuration() * durationCostWeight;
        for (int i = 0; i < routes.length; i++) {
            int numOfVehicles = routes[i].size();
            cost += numOfVehicles * numOfVehiclesCostWeight;
            if (numOfVehicles > map.maxVehiclesPerDepot)
                cost += (numOfVehicles - map.maxVehiclesPerDepot) * map.numOfCustomers * overVehicleLimitCostWeight;
        }
        return cost;
    }

    private double calculateRouteDuration(Depot depot, ArrayList<Unit> route) {
        double routeDuration = 0;
        for (int k = 0; k < route.size()-1; k++) {
            routeDuration += map.getDistance(route.get(k), route.get(k+1));
        }
        return routeDuration;
    }

    private ArrayList<Customer>[] convertRoutesToClustering(ArrayList<ArrayList<Unit>>[] routes) {
        ArrayList<Customer>[] newClustering = new ArrayList[map.numOfDepots];
        for (int i = 0; i < map.numOfDepots; i++) {
            newClustering[i] = new ArrayList<>();
            for (int j = 0; j < routes[i].size(); j++) {
                for (int k = 1; k < routes[i].get(j).size()-1; k++) {
                    if (routes[i].get(j).get(k).getClass().getSimpleName().equals("Depot"))
                        printRoutes(routes);
                    newClustering[i].add((Customer)routes[i].get(j).get(k));
                }
            }
        }
        return newClustering;
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

    void printRoutes(ArrayList<ArrayList<Unit>>[] routes) {
        for (int i = 0; i < routes.length; i++) {
            for (int j = 0; j < routes[i].size(); j++) {
                System.out.print(routes[i].get(j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


}

