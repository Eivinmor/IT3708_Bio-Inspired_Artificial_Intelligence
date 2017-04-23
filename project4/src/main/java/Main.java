import pso.PSO;
import pso.Settings;
import utility.DataReader;


public class Main {

    private enum Algorithm {PSO, ACO, BCO}
    private static Algorithm algorithm = Algorithm.PSO;

    public static void main(String[] args) {
        String[] strAr = DataReader.readOdtToStringArray(Settings.fileId);
        DataReader.makeRepresentation(strAr);
        switch (algorithm) {
            case PSO:
                PSO pso = new PSO();
                pso.runAlgorithm();
                break;
            case ACO:
                break;
            case BCO:
                break;
        }
    }
}
