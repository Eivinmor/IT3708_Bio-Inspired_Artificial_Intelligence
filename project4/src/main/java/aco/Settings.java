package aco;


import representation.JSP;

public class Settings {

    static final int colonySize = 4 * JSP.numOfJobs;

    static final double exploitationProbability = 0.8;
    static final double alpha = 1;
    static final double beta = 2;
    static final double C = 1;

    static final double basePheromone = 0.01; // TODO
    static final double pheromoneDecay = 0.5;
    static final double pheromoneEvaporation = 0.1;
}


// ÅKÆIJ SÆTTINGS

//    static final int colonySize = 4 * JSP.numOfJobs;
//
//    static final double exploitationProbability = 0;
//    static final double alpha = 1;
//    static final double beta = 1;
//    static final double C = 1;
//
//    static final double basePheromone = 0.01;
//    static final double pheromoneDecay = 0.5;
//    static final double pheromoneEvaporation = 0.07;
