package utility;

import com.panayotis.gnuplot.*;
import com.panayotis.gnuplot.dataset.Point;
import com.panayotis.gnuplot.dataset.PointDataSet;
import ga.Chromosome;
import ga.Settings;
import ga.nsga2.NSGA2Chromosome;

import java.util.ArrayList;


@SuppressWarnings("ConstantConditions")
public class Plotter {

    JavaPlot p = new JavaPlot(Settings.useOverallDeviation && Settings.useEdgeValue && Settings.useConnectivity);
    public Plotter() {
    }

    public void plotFront(ArrayList<NSGA2Chromosome> front) {
        PointDataSet<Double> d = new PointDataSet<>();

        if (Settings.useOverallDeviation && Settings.useEdgeValue && Settings.useConnectivity) {
//            p = new JavaPlot(true);
//            p.setTitle("Pareto front");
            for (Chromosome chromosome : front)
                d.add(new Point<>(chromosome.cost[0], chromosome.cost[1], chromosome.cost[2]));
        }
        else {
//            p = new JavaPlot();
//            p.setTitle("Pareto front");
            if (Settings.useOverallDeviation && Settings.useEdgeValue)
                for (Chromosome chromosome : front)
                    d.add(new Point<>(chromosome.cost[0], chromosome.cost[1]));
            else if (Settings.useOverallDeviation && Settings.useConnectivity)
                for (Chromosome chromosome : front)
                    d.add(new Point<>(chromosome.cost[0], chromosome.cost[2]));
            else if (Settings.useEdgeValue && Settings.useConnectivity)
                for (Chromosome chromosome : front)
                    d.add(new Point<>(chromosome.cost[1], chromosome.cost[2]));
        }
        p.addPlot(d);
        p.plot();
    }





}
