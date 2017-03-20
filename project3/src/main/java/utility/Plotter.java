package utility;

import com.panayotis.gnuplot.*;
import com.panayotis.gnuplot.dataset.Point;
import com.panayotis.gnuplot.dataset.PointDataSet;
import ga.Chromosome;


public class Plotter {

    JavaPlot p = new JavaPlot(true);
    PointDataSet<Double> d = new PointDataSet<>();

    public Plotter() {
        p.newGraph3D();
        p.setTitle("Pareto front");
    }

    public void addChromosome(Chromosome chromosome) {
        d.add(new Point<>(chromosome.cost[0], chromosome.cost[1], chromosome.cost[2]));
    }

    public void plot(){
        p.addPlot(d);
        p.plot();
    }




}
