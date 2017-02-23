package ga;

import representation.*;


public class Solution implements Comparable<Solution> {

    private double totalCost;
    private double weightedScore;
    private Map map;


    Solution(Map map){
        this.map = map;

    }

    @Override
    public int compareTo(Solution o) {
        return 0;
        // Weighted score basert på distance og antall kjøretøy
    }

    void generateRandomSolution() {
        for (Customer customer : map.customers) {
        }
    }



}
