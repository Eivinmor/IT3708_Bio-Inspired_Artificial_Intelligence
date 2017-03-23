package ga;


public class Settings {

    public static final int imageId = 2;
    public static final Algorithm algorithm = Algorithm.NSGA2;

    // Cost functions
    public static final boolean useOverallDeviation = true;     // 0
    public static final boolean useEdgeValue = true;            // 1
    public static final boolean useConnectivity = true;         // 2

    // NSGA2
    public static final InitialisePopulation initPop = InitialisePopulation.LIGHT;
    public static final int populationSize = 50;
    public static final int generationsPerPause = 10;
    public static final double crossoverRate = 0.5;
    public static final double mutationRate = 0.8;
    public static final ColorSpaceType colorSpace = ColorSpaceType.RGB;
    public static final boolean useTournamentForSurvivalSelection = false;

    // Mutate
    public static final double mutateMergeSegments = 0.2;
    public static final double mutateSetRandomEdgeRate = 0.2;
    public static final double mutateRemoveEdge = 0.1;
    public static final double mutateAddNewSegmentWithinThreshold = 0.4;
    public static int mutateAddNewSegmentMaxThreshold = 20;


    public static final int minimumSegmentationSize = 40;


    // Helpers
    public enum ColorSpaceType {RGB, LAB}
    public enum Algorithm {NSGA2, TEST}
    public enum InitialisePopulation{LIGHT, HEAVY}
}
