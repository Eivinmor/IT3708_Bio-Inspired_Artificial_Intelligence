package aco;

import representation.Operation;


public class Edge {
    public final Operation from, to;
    public double pheromone;


    public Edge(Operation from, Operation to) {
        this.from = from;
        this.to = to;
        this.pheromone = 0;
    }

}


