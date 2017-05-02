import ba.BA;
import ba2.BA2;
import pso.PSO;
import aco.ACO;
import utility.DataReader;


public class Main {

    private enum Algorithm {PSO, ACO, BA, BA2, TEST}
    private static Algorithm algorithm = Algorithm.BA2;
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
                BA ba = new BA();
                ba.runAlgorithm();
                break;
            case BA2:
                BA2 ba2 = new BA2();
                ba2.runAlgorithm();
                break;
            case TEST:
                break;
        }
    }
}
