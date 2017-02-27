package ga;


public class Settings {

    // GA
    static String mapName = "pr02";
    static int popSize = 100;
    static int maxIterations = 2000;
    static double elitePercent = 3;
    static int tournamentSize = 2;


    // Solution
    static double mutationRate = 0.8;
    static boolean forceNumOfVehicles = true;
    static boolean checkLoadOnClustering = true;

    static double distanceCostWeight = 1;
    static double numOfVehiclesCostWeight = 0.1;
    static double overVehicleLimitCostWeight = 10000;
    static double overDurationLimitCostWeight = 5000;
    static double overLoadLimitCostWeight = 1000000;


}
