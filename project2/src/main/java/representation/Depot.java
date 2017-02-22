package representation;


public class Depot extends Unit{

    private int number;
    private double x, y, maxRouteDuration, maxLoadPerVehicle;

    public Depot(int number, double x, double y, double maxRouteDuration, double maxLoadPerVehicle) {
        this.x = x;
        this.y = y;
        this.maxRouteDuration = maxRouteDuration;
        this.maxLoadPerVehicle = maxLoadPerVehicle;
    }

    public double getX() {return x;}

    public double getY() {return y;}

    public void printNumber(){
        System.out.println(number);
    }

}
