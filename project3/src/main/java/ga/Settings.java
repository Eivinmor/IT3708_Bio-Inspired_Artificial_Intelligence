package ga;



public class Settings {
    public static final int imageId = 1;
    public static final int populationSize = 1000;
    public static final int generationsPerPause = 100;
    public static final double crossoverRate = 0.7;
    public static final double mutationRate = 0.8;
    public static final double elitePercent = 3;
    public static final ColorSpaceType colorSpace = ColorSpaceType.RGB;

//    public static final double initSegmentDistThreshold = 30;

    // Helpers
    static int eliteSize = (int) ((elitePercent / 100) * populationSize);
    public enum ColorSpaceType {RGB, LAB}
}
