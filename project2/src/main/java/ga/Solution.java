package ga;

import representation.Map;

import java.util.ArrayList;


public class Solution implements Comparable<Solution> {

    private double totalCost;
    private double weightedScore;
    private Map map;
    ArrayList<ArrayList<ArrayList<Integer>>> depot_vehicle_routeData;       // Distance, Load, customers...


    public Solution(Map map){
        this.map = map;
    }

    @Override
    public int compareTo(Solution o) {
        return 0;
        // Weighted score basert på distance og antall kjøretøy
    }

    private boolean isValid(){
        return false;
    }

    void generateRandomSolution() {
        // Make arraylist
        ArrayList<ArrayList<Integer>> customersPerDepot = new ArrayList<>(map.numOfDepots);
        for (int i = 0; i < map.numOfDepots; i++) {
            customersPerDepot.add(new ArrayList<>());
        }
        // Assign customers to depots
        for (int i = 0; i < map.numOfCustomers; i++) {
            customersPerDepot.get(map.getClosestDepot(i)).add(i);
        }
        // Assign customers to routes


    }


}
