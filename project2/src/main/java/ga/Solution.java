package ga;

import representation.*;

import java.util.ArrayList;
import java.util.Random;


public class Solution implements Comparable<Solution>{

    // SETTINGS
    private int clusterProbExponent = Settings.clusterProbExponent;
    private double mutationRate = Settings.mutationRate;
    private boolean forceNumOfVehicles = Settings.forceNumOfVehicles;
    private double distanceCostWeight = Settings.distanceCostWeight;
    private double numOfVehiclesCostWeight = Settings.numOfVehiclesCostWeight;
    private double overVehicleLimitCostWeight = Settings.overVehicleLimitCostWeight;
    private double overDurationLimitCostWeight = Settings.overDurationLimitCostWeight;
    private double overLoadLimitCostWeight = Settings.overLoadLimitCostWeight;


    private Map map;
    private ArrayList<Customer>[] clustering;
    private ArrayList<ArrayList<Unit>>[] routes;
    private Random random = new Random();
    private double totalDistance;

    Solution(Map map) {
        this.map = map;
        clustering = clusterCustomersToDepots();
        clustering = sortClusterByCustomerDistance(clustering);
        routes = calculateAllRoutes();
        totalDistance = calculateTotalDistance();
    }

    Solution(Solution otherSolution, boolean mutate) {
        this.map = otherSolution.map;
        clustering = new ArrayList[map.numOfDepots];
        for (int i = 0; i < map.numOfDepots; i++) {
            this.clustering[i] = new ArrayList<>(otherSolution.clustering[i]);
        }
        if (mutate) mutate();
        routes = calculateAllRoutes();
        totalDistance = calculateTotalDistance();
    }

    Solution(Solution parent1, Solution parent2, boolean mutate) {
        this.map = parent1.map;
        clustering = new ArrayList[map.numOfDepots];
        for (int i = 0; i < map.numOfDepots; i++) {
            this.clustering[i] = new ArrayList<>(parent1.clustering[i]);
        }

        int randDepot = random.nextInt(map.numOfDepots);
        for (int i = 0; i < map.numOfDepots; i++) {
            clustering[i].removeAll(parent2.clustering[randDepot]);
        }
        clustering[randDepot].addAll(parent2.clustering[randDepot]);

        int randDepot2 = random.nextInt(map.numOfDepots);
        while(randDepot2 != randDepot) randDepot2 = random.nextInt(map.numOfDepots);
        for (int i = 0; i < map.numOfDepots; i++) {
            clustering[i].removeAll(parent2.clustering[randDepot2]);
        }
        clustering[randDepot2].addAll(parent2.clustering[randDepot2]);
        if (mutate) mutate();
        routes = calculateAllRoutes();
        totalDistance = calculateTotalDistance();
    }

//    Solution(Solution parent1, Solution parent2, boolean mutate) {
//
//        this.map = parent1.map;
//
//        ArrayList<Customer> parent1OneArray = new ArrayList<>();
//        ArrayList<Integer> splitList = new ArrayList<>();
//        splitList.add(0);
//        for (int i = 0; i < parent1.clustering.length; i++) {
//            parent1OneArray.addAll(parent1.clustering[i]);
//            splitList.add(parent1.clustering[i].size() + splitList.get(i));
//        }
//        ArrayList<Customer> parent2OneArray= new ArrayList<>();
//        for (int i = 0; i < parent2.clustering.length; i++) {
//            parent2OneArray.addAll(parent2.clustering[i]);
//        }
//
//        int randIndex1 = random.nextInt(parent1OneArray.size()-1);
//        int randIndex2 = random.nextInt(parent1OneArray.size() - randIndex1) + randIndex1;
//        parent2OneArray.removeAll(parent1OneArray.subList(randIndex1, randIndex2));
//
//        for (int i = 0; i < randIndex1; i++) {
//            parent1OneArray.set(i, parent2OneArray.get(i));
//        }
//        for (int i = randIndex2; i < map.numOfCustomers; i++) {
//            parent1OneArray.set(i, parent2OneArray.get(i - (randIndex2-randIndex1)));
//        }
//
//        clustering = new ArrayList[map.numOfDepots];
//        for (int i = 1; i < splitList.size(); i++) {
//            clustering[i-1] = new ArrayList<>(parent1OneArray.subList(splitList.get(i-1), splitList.get(i)));
//        }
//
//        if (mutate) mutate();
//        routes = calculateAllRoutes();
//        totalDistance = calculateTotalDistance();
//    }


//    Solution(Solution parent1, Solution parent2, boolean mutate) {
//        this.map = parent1.map;
//        clustering = new ArrayList[map.numOfDepots];
//
//        if (random.nextBoolean()) {
//            ArrayList<Customer> parent1OneArray = new ArrayList<>();
//            ArrayList<Integer> splitList = new ArrayList<>();
//            splitList.add(0);
//            for (int i = 0; i < parent1.clustering.length; i++) {
//                parent1OneArray.addAll(parent1.clustering[i]);
//                splitList.add(parent1.clustering[i].size() + splitList.get(i));
//            }
//            ArrayList<Customer> parent2OneArray= new ArrayList<>();
//            for (int i = 0; i < parent2.clustering.length; i++) {
//                parent2OneArray.addAll(parent2.clustering[i]);
//            }
//
//            int randIndex1 = random.nextInt(parent1OneArray.size()-1);
//            int randIndex2 = random.nextInt(parent1OneArray.size() - randIndex1) + randIndex1;
//            parent2OneArray.removeAll(parent1OneArray.subList(randIndex1, randIndex2));
//
//            for (int i = 0; i < randIndex1; i++) {
//                parent1OneArray.set(i, parent2OneArray.get(i));
//            }
//            for (int i = randIndex2; i < map.numOfCustomers; i++) {
//                parent1OneArray.set(i, parent2OneArray.get(i - (randIndex2-randIndex1)));
//            }
//
//            for (int i = 1; i < splitList.size(); i++) {
//                clustering[i-1] = new ArrayList<>(parent1OneArray.subList(splitList.get(i-1), splitList.get(i)));
//            }
//        }
//        else {
//            for (int i = 0; i < map.numOfDepots; i++) {
//                this.clustering[i] = new ArrayList<>(parent1.clustering[i]);
//            }
//
//            int randDepot = random.nextInt(map.numOfDepots);
//            for (int i = 0; i < map.numOfDepots; i++) {
//                clustering[i].removeAll(parent2.clustering[randDepot]);
//            }
//            clustering[randDepot].addAll(parent2.clustering[randDepot]);
//
//            int randDepot2 = random.nextInt(map.numOfDepots);
//            while(randDepot2 != randDepot) randDepot2 = random.nextInt(map.numOfDepots);
//            for (int i = 0; i < map.numOfDepots; i++) {
//                clustering[i].removeAll(parent2.clustering[randDepot2]);
//            }
//            clustering[randDepot2].addAll(parent2.clustering[randDepot2]);
//        }
//
//        if (mutate) mutate();
//        routes = calculateAllRoutes();
//        totalDistance = calculateTotalDistance();
//    }


    private ArrayList<Customer>[] clusterCustomersToDepots() {
        double[] depotFreeLoad = new double[map.numOfDepots];

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
                    depotFreeLoad[i] -= customer.demand;
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
        ArrayList<Customer> remainingCustomers = new ArrayList<>();
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

                if (forceNumOfVehicles && depotRoutes.size() == map.maxVehiclesPerDepot) {
                    remainingCustomers.addAll(depotCustomers.subList(i, depotCustomers.size()));
                    break;
                }
            }

            route.add(nextCustomer);
            duration += stepDistance + stepService;
            load += stepDemand;
        }
        route.add(depot);
        if (route.size() > 2) depotRoutes.add(route);

        if (forceNumOfVehicles) {
            for (int i = 0; i < remainingCustomers.size(); i++) {
                Customer customer = remainingCustomers.get(i);

                int bestInsertionRouteIndex = 0;
                int bestInsertionCustomerIndex = 1;
                double bestInsertionCost = Double.MAX_VALUE;
                for (int j = 0; j < depotRoutes.size(); j++) {  // Route
                    for (int k = 1; k < depotRoutes.get(j).size(); k++) {  // Customer
                        double insertionCost = getInsertionCost(customer, depotRoutes.get(j).get(k-1), depotRoutes.get(j).get(k));
                        if (insertionCost < bestInsertionCost) {
                            bestInsertionCost = insertionCost;
                            bestInsertionRouteIndex = j;
                            bestInsertionCustomerIndex = k;
                        }
                    }
                }
                ArrayList<Unit> oldDepotRoute = depotRoutes.get(bestInsertionRouteIndex);
                ArrayList<Unit> newDepotRoute = new ArrayList<>(oldDepotRoute.subList(0, bestInsertionCustomerIndex));
                newDepotRoute.add(customer);
                newDepotRoute.addAll(new ArrayList<>(oldDepotRoute.subList(bestInsertionCustomerIndex, oldDepotRoute.size())));
                depotRoutes.set(bestInsertionRouteIndex, newDepotRoute);

            }
        }
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


    public void standaloneMutate() {
        mutate();
        routes = calculateAllRoutes();
        totalDistance = calculateTotalDistance();
    }

    private void mutate() {
        if (random.nextDouble() < mutationRate) {
            double randDouble = random.nextDouble();
            if (randDouble > 0.8) betweenDepotSwap();
            else if (randDouble > 0.6) reverseMutation();
            else if (randDouble > 0.4) singleCustomerOptimalBetweenDepotReposition();
            else singleCustomerOptimalIntraDepotReposition();
        }
    }

    private void moveLastRoute() {
        ArrayList<ArrayList<Unit>>[] tempRoutes = calculateAllRoutes();
        int depotIndex = random.nextInt(map.numOfDepots);

        int cutPoint1 = random.nextInt(clustering[depotIndex].size()-1);
        int cutPoint2 = random.nextInt(clustering[depotIndex].size() - cutPoint1) + cutPoint1;
        ArrayList<Customer> reverse = new ArrayList<>(clustering[depotIndex].subList(cutPoint1, cutPoint2));
        for (int i = 0; i < reverse.size(); i++) {
            clustering[depotIndex].set(cutPoint1 + i, reverse.get(reverse.size()-1-i));
        }
    }

    private void singleCustomerOptimalIntraDepotReposition() {
        ArrayList<ArrayList<Unit>>[] tempRoutes = calculateAllRoutes();
        int depotIndex = random.nextInt(map.numOfDepots);
        int routeIndex = random.nextInt(tempRoutes[depotIndex].size());

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
                        bestInsertionCost = insertionCost;
                        bestInsertionRouteIndex = j;
                        bestInsertionCustomerIndex = k;
                    }
                }
            }
            ArrayList<Unit> chosenRoute = tempRoutes[depotIndex].get(bestInsertionRouteIndex);
            ArrayList<Unit> newRoute = new ArrayList<>(chosenRoute.subList(0, bestInsertionCustomerIndex));
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
        int depotIndex = random.nextInt(map.numOfDepots);
        while (clustering[depotIndex].size() < 2) depotIndex = random.nextInt(map.numOfDepots);

        int cutPoint1 = random.nextInt(clustering[depotIndex].size()-1);
        int cutPoint2 = random.nextInt(clustering[depotIndex].size() - cutPoint1) + cutPoint1;
        ArrayList<Customer> reverse = new ArrayList<>(clustering[depotIndex].subList(cutPoint1, cutPoint2));
        for (int i = 0; i < reverse.size(); i++) {
            clustering[depotIndex].set(cutPoint1 + i, reverse.get(reverse.size()-1-i));
        }
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

        int customer1Index = random.nextInt(clustering[depot1Index].size());
        Customer customer1 = clustering[depot1Index].get(customer1Index);

        int depot2Index = random.nextInt(map.numOfDepots);
        while (depot2Index == depot1Index) depot2Index = random.nextInt(map.numOfDepots);

        int customer2Index = random.nextInt(clustering[depot2Index].size());
        Customer customer2 = clustering[depot2Index].get(customer2Index);

        clustering[depot1Index].set(customer1Index, customer2);
        clustering[depot2Index].set(customer2Index, customer1);
    }

    
    public ArrayList<ArrayList<Unit>>[] getRoutes() {
        return routes;
    }

    
    private double calculateTotalDistance() {
        if (routes == null)
            routes = calculateAllRoutes();
        double totalDistance = 0;
        for (int i = 0; i < routes.length; i++) {
            for (int j = 0; j < routes[i].size(); j++) {
                totalDistance += calculateRouteDistance(routes[i].get(j));
            }
        }
        return totalDistance;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    double getCost() {
        double cost = getTotalDistance() * distanceCostWeight;
        for (int i = 0; i < routes.length; i++) {
            int numOfVehicles = routes[i].size();
            cost += numOfVehicles * numOfVehiclesCostWeight;
            if (numOfVehicles > map.maxVehiclesPerDepot)
                cost += (numOfVehicles - map.maxVehiclesPerDepot) * map.numOfCustomers * overVehicleLimitCostWeight;

            if (forceNumOfVehicles) {
                for (int j = 0; j < routes[i].size(); j++) {
                    double routeDemand = 0;
                    if (map.depots[i].maxRouteDuration > 0 && calculateRouteDuration(routes[i].get(j)) > map.depots[i].maxRouteDuration)
                        cost += (calculateRouteDuration(routes[i].get(j)) - map.depots[i].maxRouteDuration) * overDurationLimitCostWeight;
                    for (int k = 1; k < routes[i].get(j).size()-1; k++) {
                        Customer customer = (Customer)routes[i].get(j).get(k);
                        routeDemand += customer.demand;
                    }
                    if (routeDemand > map.depots[i].maxLoadPerVehicle)
                        cost += (routeDemand - map.depots[i].maxLoadPerVehicle) * overLoadLimitCostWeight;
                }
            }
        }
        return cost;
    }

    public double calculateRouteDuration(ArrayList<Unit> route) {
        double routeDuration = 0;
        for (int k = 0; k < route.size()-1; k++) {
            routeDuration += map.getDistance(route.get(k), route.get(k+1));
            if (k > 0) {
                Customer customer = (Customer)route.get(k);
                routeDuration += customer.serviceDuration;
            }
        }
        return routeDuration;
    }

    public double calculateRouteDistance(ArrayList<Unit> route) {
        double routeDistance = 0;
        for (int k = 0; k < route.size()-1; k++) {
            routeDistance += map.getDistance(route.get(k), route.get(k+1));
        }
        return routeDistance;
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

    @Override
    public int compareTo(Solution o) {
        if (this.getCost() < o.getCost()) return -1;
        else if (this.getCost() > o.getCost()) return 1;
        return 0;
    }
}

