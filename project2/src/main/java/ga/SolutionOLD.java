package ga;

import java.util.ArrayList;
import java.util.Random;


public class SolutionOLD implements Comparable<SolutionOLD> {

    private int numOfRoutes;
    private double totalCost;
    private ArrayList<Integer> routeDepotNumber, routeVehicleNumber;
    private ArrayList<Double> routeDuration, routeLoad;
    ArrayList<ArrayList<Integer>> routeCustomerSequence;
    private Map map;

    public SolutionOLD(Map map){
        this.map = map;
        totalCost = 0;
    }

    @Override
    public int compareTo(SolutionOLD o) {
        if (totalCost < o.getTotalCost()) return -1;
        else if (totalCost > o.getTotalCost()) return 1;
        else if (numOfRoutes < o.getNumOfRoutes()) return -1;
        else if (numOfRoutes > o.getNumOfRoutes()) return 1;
        return 0;
    }

    private boolean isValid(){
        return false;
    }

    double getTotalCost() {return totalCost;}

    int getNumOfRoutes() {return numOfRoutes;}

//    void generateRandomSolution() {
//        Random random = new Random();
//        ArrayList<ArrayList<Integer>> depotVehicleLoad = new ArrayList<>(map.numOfDepots);
//        for (int i = 0; i < map.numOfDepots; i++) {
//            depotVehicleLoad.add(new ArrayList<>());
//        }
//    }


}
