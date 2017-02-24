package ga;

import representation.Customer;
import representation.Depot;
import representation.Map;
import representation.Unit;

import java.util.ArrayList;
import java.util.Random;


public class SolutionOLD implements Comparable<SolutionOLD> {

    private Map map;
    private ArrayList<Customer>[] depotCustomersArray;
    private ArrayList<Unit>[] depotRoutesArray;

    SolutionOLD(Map map){
        this.map = map;
        depotCustomersArray = new ArrayList[map.numOfDepots];
        depotRoutesArray = new ArrayList[map.numOfDepots];
        generateInitialSolution();
    }

    SolutionOLD(SolutionOLD cloneSolution) {
        this.map = cloneSolution.map;

        this.depotCustomersArray = new ArrayList[map.numOfDepots];
        for (int i = 0; i < cloneSolution.depotCustomersArray.length; i++) {
            this.depotCustomersArray[i] = new ArrayList<>(cloneSolution.depotCustomersArray[i]);
        }
        this.depotRoutesArray = new ArrayList[map.numOfDepots];
        for (int i = 0; i < cloneSolution.depotRoutesArray.length; i++) {
            this.depotRoutesArray[i] = new ArrayList<>(cloneSolution.depotRoutesArray[i]);
        }
    }

    public int compareTo(SolutionOLD o) {
        return 0;
        // Weighted score basert på distance og antall kjøretøy
    }

    void generateInitialSolution() {
        depotCustomersArray = clustering(-10);
        depotRoutesArray = routingAndScheduling();
//        mutate();
    }

    ArrayList<Customer>[] clustering(int probExponent) {

        // Set exponential probability to be assigned to depot based on distance
        Random random = new Random();
        ArrayList<Customer>[] clusteredCustomers = new ArrayList[map.numOfDepots];
        for (int i = 0; i < map.numOfDepots; i++) {
            clusteredCustomers[i] = new ArrayList<>();
        }
        for (Customer customer : map.customers) {
            double[] depotProb = new double[map.numOfDepots];
            // Find distance and calc probability
            double allDepotDistances = 0;
            for (int i = 0; i < map.numOfDepots; i++) {
                allDepotDistances += Math.pow(map.getDistance(customer, map.depots[i]), probExponent);
            }
            for (int i = 0; i < map.numOfDepots; i++) {
                double distance = map.getDistance(customer, map.depots[i]);
                double prob = Math.pow(distance, probExponent)/allDepotDistances;
                depotProb[i] = prob;
            }
            // Choose depot
            double randValue = random.nextDouble();
            for (int i = 0; i < map.numOfDepots; i++) {
                if (depotProb[i] > randValue) {
                    clusteredCustomers[i].add(customer);
                    break;
                }
                randValue -= depotProb[i];
            }
        }
        return clusteredCustomers;
    }

    private ArrayList<Unit>[] routingAndScheduling() {
        ArrayList<Unit>[] depotRoutesArray = new ArrayList[map.numOfDepots];
        for (int i = 0; i < map.numOfDepots; i++) {
            Depot depot = map.depots[i];
            ArrayList<Customer> depotCustomersPool = new ArrayList<>(depotCustomersArray[i]);
            ArrayList<Unit> depotRoutes = new ArrayList<>();
            depotRoutes.add(depot);

            double maxDuration = depot.maxRouteDuration;
            double maxLoad = depot.maxLoadPerVehicle;
            double duration = 0;
            double load = 0;

            for (int j = 0; j < depotCustomersArray[i].size(); j++) {
                // Find closest next customer
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

                if ( load + closestCustomer.demand > maxLoad || (maxDuration > 0 && duration + map.getDistance(depot,
                        closestCustomer) > maxDuration)) {
                    depotRoutes.add(depot);
                    duration += map.getDistance(lastUnit, depot);
                    duration = map.getDistance(depot, closestCustomer);
                    load = 0;
                }
                depotRoutes.add(closestCustomer);
                load += closestCustomer.demand;
                duration += closestCustomer.serviceDuration + shortestDistance;
            }
            duration += map.getDistance(depotRoutes.get(depotRoutes.size()-1), depot);
            depotRoutes.add(depot);
            depotRoutesArray[i] = depotRoutes;
        }
        return depotRoutesArray;
    }

    void mutate() {
        Random random = new Random();

        if (random.nextBoolean()) {
            for (int i = 0; i < map.numOfDepots; i++) {
                int swapDepot1 = random.nextInt(map.numOfDepots);
                int swapId1 = random.nextInt(depotCustomersArray[swapDepot1].size());

                Customer swapUnit1 = depotCustomersArray[swapDepot1].get(swapId1);

                int swapDepot2 = random.nextInt(map.numOfDepots);
                int swapId2 = random.nextInt(depotCustomersArray[swapDepot2].size());
                Customer swapUnit2 = depotCustomersArray[swapDepot2].get(swapId2);

                depotCustomersArray[swapDepot1].set(swapId1, swapUnit2);
                depotCustomersArray[swapDepot2].set(swapId2, swapUnit1);
            }
        }
        depotRoutesArray = routingAndScheduling();
        int randomDepot = random.nextInt(map.numOfDepots);
        int randomRouteUnit1 = random.nextInt(depotRoutesArray[randomDepot].size());
        int randomRouteUnit2 = random.nextInt(depotRoutesArray[randomDepot].size());
        Unit swapUnit1 = depotRoutesArray[randomDepot].get(randomRouteUnit1);
        Unit swapUnit2 = depotRoutesArray[randomDepot].get(randomRouteUnit2);
        depotRoutesArray[randomDepot].set(randomRouteUnit1, swapUnit2);
        depotRoutesArray[randomDepot].set(randomRouteUnit2, swapUnit1);
    }

    public double getTotalDistance() {
        double distance = 0;
        for (ArrayList<Unit> route : depotRoutesArray) {
            for (int i = 0; i < route.size()-1; i++) {
                distance += map.getDistance(route.get(i), route.get(i+1));
            }
        }
        return distance;
    }



    public ArrayList<Unit>[] getRoutes() {
        return depotRoutesArray;
    }

}
