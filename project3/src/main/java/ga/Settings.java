package ga;

public class Settings {

    public static int imageId = 1;
    static int populationSize = 100;
    static int generationsPerPause = 100;
    static double crossoverRate = 0.8;
    static double mutationRate = 0.7;
    static double elitePercent = 3;

    static double initSegmentDistThreshold = 40;
    static double initSegmentDistThresholdVariance = 0.5;
    static double initSegmentMinimumSize = 20;

    static int eliteSize = (int)(elitePercent/100)*populationSize;
}
