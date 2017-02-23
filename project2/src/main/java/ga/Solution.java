package ga;

import representation.*;


public class Solution implements Comparable<Solution> {

    private double totalCost;
    private double weightedScore;
    private Map map;
    private SolutionDepot[] solutionDepots;


    Solution(Map map){
        this.map = map;
        solutionDepots = new SolutionDepot[map.numOfDepots];
        for (Depot depot : map.depots) {
            solutionDepots[depot.number-1] = new SolutionDepot(depot);
        }
    }

    @Override
    public int compareTo(Solution o) {
        return 0;
        // Weighted score basert på distance og antall kjøretøy
    }

    void generateRandomSolution() {
        for (Customer customer : map.customers) {
            solutionDepots[map.getClosestDepot(customer).number-1].addCustomer(customer);
        }
        for (SolutionDepot solutionDepot : solutionDepots) {
            for (Customer customer : solutionDepot.getCustomers()) {
                solutionDepot.addCustomerToCurrentRoute(customer);
            }
        }
    }

    public SolutionDepot[] getSolutionDepots() {
        return solutionDepots;
    }



}
