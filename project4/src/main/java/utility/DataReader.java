package utility;

import java.io.*;

import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.common.TextExtractor;
import representation.*;

public abstract class DataReader {

    private static String filePathRoot = System.getProperty("user.dir") + "/src/main/resources/input/";

    public static String[] readOdtToStringArray(int fileId) {
        try {
            File odtFile = new File(filePathRoot + fileId + ".odt");
            TextDocument xmlDoc = TextDocument.loadDocument(odtFile);
            String textDoc = TextExtractor.getText(xmlDoc.getContentRoot());
            return textDoc.split("\\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void makeRepresentation(String[] stringArray) {
        String[] lineStringArray = stringArray[0].trim().replaceAll(" +", " ").split(" ");
        JSP.numOfJobs = Integer.parseInt(lineStringArray[0]);
        JSP.numOfMachines = Integer.parseInt(lineStringArray[1]);
        JSP.jobs = new Operation[JSP.numOfJobs][JSP.numOfMachines];
        for (int i = 1; i < stringArray.length; i++) {
            lineStringArray = stringArray[i].trim().replaceAll(" +", " ").split(" ");
            for (int j = 0; j < lineStringArray.length; j+=2) {
                int machine = Integer.parseInt(lineStringArray[j]);
                int duration = Integer.parseInt(lineStringArray[j+1]);
                Operation op = new Operation(i-1, machine, duration);
                JSP.jobs[i-1][j/2] = op;
            }
        }
    }

}
