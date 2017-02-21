import java.util.ArrayList;


public class Solution {

    private int[] depotNumbers, vehicleNumbers, vehicleLoad;
    private double[] routeDurations;
    private ArrayList<ArrayList<Integer>> sequences;

    public Solution(int[] depotNumbers, int[] vehicleNumbers, double[] routeDurations,
                    int[] vehicleLoad, ArrayList<ArrayList<Integer>> sequences){
        this.depotNumbers = depotNumbers;
        this.vehicleNumbers = vehicleNumbers;
        this.routeDurations = routeDurations;
        this.vehicleLoad = vehicleLoad;
        this.sequences = sequences;
    }
}
