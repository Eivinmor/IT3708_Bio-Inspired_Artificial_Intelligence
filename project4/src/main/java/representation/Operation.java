package representation;


public class Operation {

    public final int job, machine;
    public final double duration;

    public Operation(int job, int machine, double duration) {
        this.job = job;
        this.machine = machine;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "O={m" + machine + " j" + job + " d" + duration + "}";
    }
}
