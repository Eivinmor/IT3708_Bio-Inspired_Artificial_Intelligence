package representation;


public class Operation {

    public final int job, machine, duration;

    public Operation(int job, int machine, int duration) {
        this.job = job;
        this.machine = machine;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "O={ j:" + job + " m:" + machine + " d:" + duration + "}";
    }
}
