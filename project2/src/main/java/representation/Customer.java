package representation;


public class Customer implements Unit{

    public final int number;
    public final double serviceDuration, demand;
    private double x, y;

    public Customer(int number, double x, double y, double serviceDuration, double demand) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.serviceDuration = serviceDuration;
        this.demand = demand;
    }

    public double getX() {return x;}

    public double getY() {return y;}

    public String toString() {return Integer.toString(number);}


}
