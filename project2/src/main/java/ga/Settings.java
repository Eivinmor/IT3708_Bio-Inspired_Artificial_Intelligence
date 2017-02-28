package ga;


public class Settings {

    // GA
    static String mapName = "pr06";
    static int popSize = 100;
    static int maxIterations = 100000;
    static double elitePercent = 3;
    static int tournamentSize = 2;
    static double crossoverRate = 0.8;


    // Solution
    static int clusterProbExponent = -10;
    static double mutationRate = 0.8;
    static boolean forceNumOfVehicles = true;
//    static boolean checkLoadOnClustering = true;

    static double distanceCostWeight = 1;
    static double numOfVehiclesCostWeight = 0.1;
    static double overVehicleLimitCostWeight = 10000;
    static double overDurationLimitCostWeight = 5000;
    static double overLoadLimitCostWeight = 1000000;


    // 1. CHANGE MUTATION WEIGHTS IN Solution (inter/intra depot)
    // 2. CHANGE CROSSOVER IN Solution
    // 3. CHANGE POPULATION SELECTION IN GA

}
