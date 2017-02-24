package ga;

import representation.*;

import java.util.ArrayList;
import java.util.Random;


public class Solution implements Comparable<Solution> {

    private double totalDistance, weightedScore;
    private Map map;
    private ArrayList<Customer>[] depotCustomersArray;
    private ArrayList<Unit>[] depotRoutesArray;

    Solution(Map map){
        this.map = map;
        depotCustomersArray = new ArrayList[map.numOfDepots];
        depotRoutesArray = new ArrayList[map.numOfDepots];
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
        depotCustomersArray = clustering();
        routingAndScheduling();
//        mutate();
    }

    ArrayList<Customer>[] clustering() {
//        for (Customer customer : map.customers) {
//            depotCustomersArray[map.getClosestDepot(customer).number-1].add(customer);
//        }
        // Set exponential probability to be assigned to depot based on distance
        Random random = new Random();
        int exponent = -3;
        ArrayList<Customer>[] clusteredCustomers = new ArrayList[map.numOfDepots];
        for (int i = 0; i < map.numOfDepots; i++) {
            clusteredCustomers[i] = new ArrayList<>();
        }
        for (Customer customer : map.customers) {
            double[] depotProb = new double[map.numOfDepots];
            // Find distance and calc probability
            double allDepotDistances = 0;
            for (int i = 0; i < map.numOfDepots; i++) {
                allDepotDistances += Math.pow(map.getDistance(customer, map.depots[i]), exponent);
            }
            for (int i = 0; i < map.numOfDepots; i++) {
                double distance = map.getDistance(customer, map.depots[i]);
                double prob = Math.pow(distance, exponent)/allDepotDistances;
                depotProb[i] = prob;
            }
            // Choose depot
            double randValue = random.nextDouble();
            for (int i = 0; i < map.numOfDepots; i++) {
                System.out.println(depotProb[i] + ", " + randValue);
                if (depotProb[i] > randValue) {
                    clusteredCustomers[i].add(customer);
                    System.out.println("YES");
                    break;
                }
                randValue -= depotProb[i];
            }
        }
        return clusteredCustomers;
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
                    System.out.println(duration);
                    duration += map.getDistance(lastUnit, depot);
                    totalDistance += duration;
                    duration = map.getDistance(depot, closestCustomer);
                    load = 0;
                }
                depotRoutes.add(closestCustomer);
                load += closestCustomer.demand;
                duration += closestCustomer.serviceDuration + shortestDistance;
            }
            depotRoutes.add(depot);
            duration += map.getDistance(depotRoutes.get(depotRoutes.size()-1), depot);
            totalDistance += duration;
            depotRoutesArray[i] = depotRoutes;
        }
    }

    void mutate() {
        Random random = new Random();
        for (int i = 0; i < map.numOfDepots; i++) {
            int swapId1 = random.nextInt(depotRoutesArray[i].size());
            Unit swapUnit1 = depotRoutesArray[i].get(swapId1);
            int swapId2 = random.nextInt(depotRoutesArray[i].size());
            Unit swapUnit2 = depotRoutesArray[i].get(swapId2);
            depotRoutesArray[i].set(swapId1, swapUnit2);
            depotRoutesArray[i].set(swapId2, swapUnit1);
        }
    }

    public double getTotalDistance() {
        return totalDistance;
    }



    public ArrayList<Unit>[] getRoutes() {
        return depotRoutesArray;
    }

}
