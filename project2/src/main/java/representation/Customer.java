package representation;


public class Customer extends Unit{

    private int number;
    private double x, y, serviceDuration, demand;

    public Customer(int number, double x, double y, double serviceDuration, double demand) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.serviceDuration = serviceDuration;
        this.demand = demand;
    }

    public double getX() {return x;}

    public double getY() {return y;}

    public void printNumber(){
        System.out.println(number);
    }


}
