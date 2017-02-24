package ga;

import representation.*;

import java.util.ArrayList;
import java.util.Random;


public class Solution {

    private Map map;
    private ArrayList<Customer>[] clustering;
    private ArrayList<ArrayList<Customer>>[] routes;
    private Random random;
    private int clusterProbExponent;

    public Solution(Map map) {

        // SETTINGS
        clusterProbExponent = -10;


        this.map = map;
        random = new Random();
        clustering = clusterCustomersToDepots();
        routes = calculateAllRoutes();

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

    private ArrayList<ArrayList<Customer>>[] calculateAllRoutes() {
        ArrayList<ArrayList<Customer>>[] allRoutes = new ArrayList[map.numOfDepots];
        for (int i = 0; i < map.numOfDepots; i++) {
            allRoutes[i] = calculateDepotRoutes(i);
        }
        return allRoutes;
    }

    private ArrayList<ArrayList<Customer>> calculateDepotRoutes(int depotNumber) {
        Depot depot = map.depots[depotNumber];
        ArrayList<ArrayList<Customer>> depotRoutes = new ArrayList<>();
        ArrayList<Customer> depotCustomersPool = new ArrayList<>(clustering[depotNumber]);
        double maxDuration = depot.maxRouteDuration;
        double maxLoad = depot.maxLoadPerVehicle;
        double duration = 0;
        double load = 0;

        ArrayList<Customer> route = new ArrayList<>();
        Customer firstCustomer =  findClosestCustomer(depot, depotCustomersPool);
        route.add(firstCustomer);
        depotCustomersPool.remove(firstCustomer);

        while (depotCustomersPool.size() > 0) {
            Customer lastCustomer = route.get(route.size()-1);
            Customer closestCustomer = findClosestCustomer(lastCustomer, depotCustomersPool);
            double stepDistance = map.getDistance(lastCustomer, closestCustomer);
            double depotDistance = map.getDistance(closestCustomer, depot);
            double stepService = closestCustomer.serviceDuration;
            double stepDemand = closestCustomer.demand;
            if ((maxDuration > 0 && duration + stepDistance + depotDistance + stepService > maxDuration)
                    || load + stepDemand > maxLoad) {
                depotRoutes.add(route);
                route = new ArrayList<>();
                closestCustomer = findClosestCustomer(depot, depotCustomersPool);
                duration = 0;
                load = 0;
            }
            route.add(closestCustomer);
//            for (int i = 0; i < route.size(); i++) {
//                System.out.print(route.get(i) + " ");
//            }
//            System.out.println();
            duration += stepDistance + stepService;
            load += stepDemand;
            depotCustomersPool.remove(closestCustomer);
//            for (int i = 0; i < depotCustomersPool.size(); i++) {
//                System.out.print(depotCustomersPool.get(i) + " ");
//            }
//            System.out.println();
        }
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

    public ArrayList<ArrayList<Unit>>[] getRoutes() {
        ArrayList<ArrayList<Unit>>[] routesWithDepot = new ArrayList[map.numOfDepots];
        for (int i = 0; i < routes.length; i++) {
            ArrayList<ArrayList<Unit>> depot = new ArrayList<>();
            for (int j = 0; j < routes[i].size(); j++) {
                ArrayList<Unit> route = new ArrayList<>();
                route.add(map.depots[i]);
                route.addAll(routes[i].get(j));
                route.add(map.depots[i]);
                for (int k = 0; k < route.size(); k++) {
                    System.out.print(route.get(k) + " ");
                }
                System.out.println();
                depot.add(route);
            }
            routesWithDepot[i] = depot;
        }
        return routesWithDepot;
    }

    double getTotalDuration() {
        double totalDuration = map.totalServiceDuration;
        for (int i = 0; i < routes.length; i++) {
            Depot depot = map.depots[i];
            for (int j = 0; j < routes[i].size(); j++) {
                totalDuration += map.getDistance(depot, routes[i].get(j).get(0));
                for (int k = 0; k < routes[i].get(j).size()-1; k++) {
                    totalDuration += map.getDistance(routes[i].get(j).get(k), routes[i].get(j).get(k+1));
                }
                totalDuration += map.getDistance(routes[i].get(j).get(routes[i].get(j).size()-1), depot);
            }
        }
        return totalDuration;
    }
}

