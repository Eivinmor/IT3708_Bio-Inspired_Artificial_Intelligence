import ga.NSGA2;
import ga.Settings;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        switch (Settings.algorithm) {
            case NSGA2:
                NSGA2 nsga2 = new NSGA2();
                nsga2.runAlgorithm();
            case PAES:
                break;
        }
    }
}
