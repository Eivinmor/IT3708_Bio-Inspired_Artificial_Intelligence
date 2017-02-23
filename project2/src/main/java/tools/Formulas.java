package tools;


import representation.Unit;

public abstract class Formulas {

    public static double euclideanDistance(Unit a, Unit b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }
}
