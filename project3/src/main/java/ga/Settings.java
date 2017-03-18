package ga;


public class Settings {

    public static int imageId = 8;
    public static int populationSize = 100;
    public static int generationsPerPause = 100;
    public static double crossoverRate = 0.7;
    public static double mutationRate = 0.8;
    public static double elitePercent = 3;

    static double initSegmentDistThreshold = 30;


    static int eliteSize = (int)(elitePercent/100)*populationSize;
    public static int numOfNeighbours = 4;
}
