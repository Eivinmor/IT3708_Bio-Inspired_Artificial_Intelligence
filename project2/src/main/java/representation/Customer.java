package representation;


public class Customer {

    private double x, y, serviceDuration, demand;

    public Customer(double x, double y, double serviceDuration, double demand) {
        this.x = x;
        this.y = y;
        this.serviceDuration = serviceDuration;
        this.demand = demand;
    }

    public double getX() {return x;}

    public double getY() {return y;}


}
