package representation;


public class Customer extends Unit{

    public final double serviceDuration, demand;

    public Customer(int number, double x, double y, double serviceDuration, double demand) {
        super(number, x, y);
        this.serviceDuration = serviceDuration;
        this.demand = demand;
    }

}
