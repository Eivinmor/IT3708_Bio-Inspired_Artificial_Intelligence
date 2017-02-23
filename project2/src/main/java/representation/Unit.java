package representation;


public abstract class Unit {

    public final int number;
    public final double x, y;

    public Unit(int number, double x, double y) {
        this.number = number;
        this.x = x;
        this.y = y;
    }

    public String toString() {return Integer.toString(number);}
}
