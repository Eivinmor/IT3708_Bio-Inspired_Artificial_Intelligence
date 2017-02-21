package ga;

import java.util.ArrayList;


public class Solution implements Comparable<Solution> {

    private int numOfRoutes;
    private double totalCost;
    private ArrayList<Integer> routeDepotNumber, routeVehicleNumber;
    private ArrayList<Double> routeDuration, routeLoad;
    ArrayList<ArrayList<Integer>> routeCustomerSequence;

    public Solution(){
        totalCost = 0;
    }

    @Override
    public int compareTo(Solution o) {
        if (totalCost < o.getTotalCost()) return -1;
        else if (totalCost > o.getTotalCost()) return 1;
        else if (numOfRoutes < o.getNumOfRoutes()) return -1;
        else if (numOfRoutes > o.getNumOfRoutes()) return 1;
        return 0;
    }

    double getTotalCost() {return totalCost;}

    int getNumOfRoutes() {return numOfRoutes;}

}
