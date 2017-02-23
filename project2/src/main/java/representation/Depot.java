package representation;


public class Depot implements Unit{

    public final int number;
    public final double maxRouteDuration, maxLoadPerVehicle;
    private final double x, y;

    public Depot(int number, double x, double y, double maxRouteDuration, double maxLoadPerVehicle) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.maxRouteDuration = maxRouteDuration;
        this.maxLoadPerVehicle = maxLoadPerVehicle;
    }

    public double getX() {return x;}

    public double getY() {return y;}

    public String toString() {return Integer.toString(number);}

}
