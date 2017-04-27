import pso.PSO;
import aco.ACO;
import utility.DataReader;


public class Main {

    private enum Algorithm {PSO, ACO, BCO, TEST}
    private static Algorithm algorithm = Algorithm.ACO;
    public static final int fileId = 2;

    public static void main(String[] args) {
        String[] strAr = DataReader.readOdtToStringArray(fileId);
        DataReader.makeRepresentation(strAr);
        switch (algorithm) {
            case PSO:
                PSO pso = new PSO();
                pso.runAlgorithm();
                break;
            case ACO:
                ACO aco = new ACO();
                aco.runAlgorithm();
                break;
            case BCO:
                break;
            case TEST:
                ACO aco2 = new ACO();
                aco2.runAlgorithm();
                break;
        }
    }
}
