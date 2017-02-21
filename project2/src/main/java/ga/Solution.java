package ga;

import java.util.ArrayList;
import java.util.Random;


public class Solution implements Comparable<Solution> {

    private Map map;
    ArrayList<ArrayList<Double>> depot_vehicle_routeData;


    public Solution(Map map){
        this.map = map;
    }

    @Override
    public int compareTo(Solution o) {
        return 0;
    }

    private boolean isValid(){
        return false;
    }

    void generateRandomSolution() {
        depot_vehicle_routeData = new ArrayList<>(map.numOfDepots);
        Random random = new Random();
        int customerCount = 0;
        for (int i = 0; i < map.numOfCustomers; i++) {
            int depot = random.nextInt(map.numOfDepots);
            int vehicle = random.nextInt(map.maxVehiclesPerDepot);

        }
    }


}
