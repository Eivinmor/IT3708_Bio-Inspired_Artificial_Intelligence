package representation;


public class Operation {

    public final int job, machine, jobOpIndex;
    public final double duration;

    public Operation(int job, int machine, double duration, int jobOpIndex) {
        this.job = job;
        this.machine = machine;
        this.duration = duration;
        this.jobOpIndex = jobOpIndex;
    }

    @Override
    public String toString() {
        return "O={m" + machine + " j" + job + " d" + duration + "}";
    }
}
