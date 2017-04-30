import ba.BA;
import pso.PSO;
import aco.ACO;
import utility.DataReader;


public class Main {

    private enum Algorithm {PSO, ACO, BA, TEST}
    private static Algorithm algorithm = Algorithm.BA;
    public static final int fileId = 32;

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
            case BA:
                BA ba = new BA();
                ba.runAlgorithm();
                break;
            case TEST:
                ACO aco2 = new ACO();
                aco2.runAlgorithm();
                break;
        }
    }
}
