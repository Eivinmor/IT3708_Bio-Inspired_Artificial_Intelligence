import abc.ABC;
import ba.BA;
import pso.PSO;
import aco.ACO;
import utility.DataReader;


public class Main {

    private enum Algorithm {PSO, ACO, BA, ABC, TEST}
    private static Algorithm algorithm = Algorithm.PSO;
    public static final int fileId = 3;

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
                BA ba2 = new BA();
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
