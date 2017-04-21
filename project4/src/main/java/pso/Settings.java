package pso;


public abstract class Settings {

    public static final int fileId = 32;
    static final int numOfParticles = 1000;
    static final double gBestFactor = 0.3;
    static final double pBestFactor = 0.5;
    static final double mutationRate = 0.9;

    static final double inertiaWeight = 0.5;
}
