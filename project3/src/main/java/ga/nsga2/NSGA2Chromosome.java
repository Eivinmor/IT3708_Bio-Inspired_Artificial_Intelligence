package ga.nsga2;

import ga.Chromosome;

import java.util.Comparator;
import java.util.HashSet;


public class NSGA2Chromosome extends Chromosome {

    public int numOfDominators;
    public HashSet<NSGA2Chromosome> dominates;
    public int rank;
    public double crowdingDistance;

    public NSGA2Chromosome() {
        super();
    }

    public NSGA2Chromosome(Chromosome clonosome) {
        super(clonosome);
    }

    public NSGA2Chromosome(Chromosome c1, Chromosome c2) {
        super(c1, c2);
    }

    static Comparator<NSGA2Chromosome> crowdingDistanceComparator() {
        return (o1, o2) -> {
            if (o1.crowdingDistance > o2.crowdingDistance) return -1;
            if (o1.crowdingDistance < o2.crowdingDistance) return 1;
            return 0;
        };
    }

    static Comparator<NSGA2Chromosome> nonDominationRankAndCrowdingDistanceComparator() {
        return (o1, o2) -> {
            if (o1.rank < o2.rank) return -1;
            if (o1.rank > o2.rank) return 1;
            if (o1.crowdingDistance > o2.crowdingDistance) return -1;
            if (o1.crowdingDistance < o2.crowdingDistance) return 1;
            return 0;
        };
    }

    static Comparator<NSGA2Chromosome> overallDeviationComparator() {
        return (o1, o2) -> {
            if (o1.cost[0] < o2.cost[0]) return -1;
            if (o1.cost[0] > o2.cost[0]) return 1;
            return 0;
        };
    }

    static Comparator<NSGA2Chromosome> edgeValueComparator() {
        return (o1, o2) -> {
            if (o1.cost[1] < o2.cost[1]) return -1;
            if (o1.cost[1] > o2.cost[1]) return 1;
            return 0;
        };
    }

    static Comparator<NSGA2Chromosome> connectivityComparator() {
        return (o1, o2) -> {
            if (o1.cost[2] < o2.cost[2]) return -1;
            if (o1.cost[2] > o2.cost[2]) return 1;
            return 0;
        };
    }
}

