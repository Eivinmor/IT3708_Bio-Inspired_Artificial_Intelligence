package ga;


public class Settings {
    public static final int RGB = 0;
    public static final int Lab = 1;

    // ---- SETTINGS ----------------------------------
    public static final int imageId = 1;
    public static final int populationSize = 100;
    public static final int generationsPerPause = 100;
    public static final double crossoverRate = 0.7;
    public static final double mutationRate = 0.8;
    public static final double elitePercent = 3;
    public static final int colorSpace = RGB;

    public static final double initSegmentDistThreshold = 30;
    // ------------------------------------------------

    // Helpers
    static int eliteSize = (int) ((elitePercent / 100) * populationSize);

}
