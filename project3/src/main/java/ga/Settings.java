package ga;



public class Settings {

    public static final int imageId = 1;
    public static final Algorithm algorithm = Algorithm.TEST;

    // MOEA
    public static final int populationSize = 1000;
    public static final int generations = 100;
    public static final int generationsPerPause = 100;
    public static final double crossoverRate = 0.7;
    public static final double mutationRate = 0.8;
    public static final ColorSpaceType colorSpace = ColorSpaceType.RGB;

    // Cost functions
    public static final boolean useOverallDeviation = true;     // 0
    public static final boolean useEdgeValue = true;            // 1
    public static final boolean useConnectivity = true;         // 2

    // Chromosome
    public static final double mutateAddEdgeRate = 0.2;
    public static final double mutateSetRandomEdgeRate = 0.4;
    public static final double mutateRemoveEdge = 0.4;

    // Output
    public static final boolean drawBorders = true;


    // Currently unused
    public static final double initSegmentDistThreshold = 30;

    // Helpers
    public enum ColorSpaceType {RGB, LAB}
    public enum Algorithm {PAES, NSGA2, TEST}
}
