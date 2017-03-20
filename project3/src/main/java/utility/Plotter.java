package utility;

import com.panayotis.gnuplot.*;
import com.panayotis.gnuplot.dataset.Point;
import com.panayotis.gnuplot.dataset.PointDataSet;
import ga.Chromosome;
import ga.nsga2.NSGA2Chromosome;

import java.util.ArrayList;


public class Plotter {

    JavaPlot p = new JavaPlot(true);

    public Plotter() {
        p.newGraph3D();
        p.setTitle("Pareto front");
    }

    public void plotFront(ArrayList<NSGA2Chromosome> front) {
        PointDataSet<Double> d = new PointDataSet<>();
        for (Chromosome chromosome : front) {
            d.add(new Point<>(chromosome.cost[0], chromosome.cost[1], chromosome.cost[2]));
        }
        p.addPlot(d);
        p.plot();
    }





}
