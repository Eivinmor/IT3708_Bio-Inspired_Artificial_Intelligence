package ba2;

class Settings {

    static final int populationSize = 200;       // n
    static final int numOfScoutBees = 100;      // ns

    static final int numOfEliteSites = populationSize/10;           // ne
    static final int numOfBestSites = populationSize/3;            // nb

    static final int beesPerEliteSite = 50;      // nre
    static final int beesPerBestSite = 20;       // nrb

    static final double initialNeighbourhoodSize = 1;                  // ngh
    static final double neighbourhoodReduction = 0.05;

    static final int numOfStagnationRoundsBeforeAbandonment = 5;        // stlim



}
