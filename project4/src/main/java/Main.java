import abc.ABC;
import ba2.BA2;
import pso.PSO;
import aco.ACO;
import utility.DataReader;


public class Main {

    private enum Algorithm {PSO, ACO, BA2, ABC, TEST}
    private static Algorithm algorithm = Algorithm.ABC;
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
            case BA2:
                BA2 ba2 = new BA2();
                ba2.runAlgorithm();
                break;
            case ABC:
                ABC ba = new ABC();
                ba.runAlgorithm();
                break;
            case TEST:
                break;
        }
    }
}
