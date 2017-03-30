import representation.JSP;
import representation.Operation;
import utility.DataReader;


public class Main {

    public static void main(String[] args) {
        String[] strAr = DataReader.readOdtToStringArray(1);
        DataReader.makeRepresentation(strAr);
    }

}
