package representation;


public class Depot extends Unit{

    public final double maxRouteDuration, maxLoadPerVehicle;

    public Depot(int number, double x, double y, double maxRouteDuration, double maxLoadPerVehicle) {
        super(number, x, y);
        this.maxRouteDuration = maxRouteDuration;
        this.maxLoadPerVehicle = maxLoadPerVehicle;
    }

}
