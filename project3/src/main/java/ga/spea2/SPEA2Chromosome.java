package ga.spea2;

import ga.Chromosome;


public class SPEA2Chromosome extends Chromosome{

    public int rank;

    public SPEA2Chromosome() {
        super();
    }

    public SPEA2Chromosome(Chromosome clonosome) {
        super(clonosome);
    }

    public SPEA2Chromosome(Chromosome c1, Chromosome c2) {
        super(c1, c2);
    }

}
