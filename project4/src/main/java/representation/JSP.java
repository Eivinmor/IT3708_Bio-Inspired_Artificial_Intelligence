package representation;

public abstract class JSP {

    public static Operation[][] jobs;
    public static int numOfMachines, numOfJobs, numOfOperations;

    public static Operation getOperation(int index) {
        return JSP.jobs[index / JSP.numOfMachines][index % JSP.numOfMachines];
    }

}
