package ga.nsga2;

import ga.Chromosome;
import java.util.HashSet;


public class NSGA2Chromosome extends Chromosome implements Comparable<NSGA2Chromosome> {

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

    @Override
    public int compareTo(NSGA2Chromosome o) {
//        if (rank < o.rank) return -1;
//        if (rank > o.rank) return 1;
        if (crowdingDistance > o.crowdingDistance) return -1;
        if (crowdingDistance < o.crowdingDistance) return 1;
        return 0;
    }
}
