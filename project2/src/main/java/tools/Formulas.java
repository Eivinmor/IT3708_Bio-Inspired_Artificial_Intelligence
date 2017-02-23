package tools;

import representation.Unit;


public abstract class Formulas {

    public static double euclideanDistance(Unit a, Unit b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

}
