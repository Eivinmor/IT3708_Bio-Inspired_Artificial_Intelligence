package utility;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.PlotOrientation;

import org.jfree.chart.plot.XYPlot;

import org.jfree.chart.renderer.xy.GradientXYBarPainter;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYIntervalDataItem;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.TextAnchor;
import representation.JSP;
import representation.Operation;

import java.awt.*;


public class Plotter extends ApplicationFrame {

    // TODO Add (op order), (job id) labels for operation bars

    String[] machines, jobs;
    XYIntervalSeriesCollection dataset;

    public Plotter() {

        super("Schedule");

        dataset = new XYIntervalSeriesCollection();

        machines = new String[JSP.numOfMachines];
        for(int i = 0; i < JSP.numOfMachines; i++) machines[i] = "M" + (i + 1);

        jobs = new String[JSP.numOfJobs];
        for(int i = 0; i < JSP.numOfJobs; i++) jobs[i] = "J" + (i + 1);

        XYBarRenderer renderer = new XYBarRenderer();
        renderer.setUseYInterval(true);
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardXYBarPainter());
        renderer.setDrawBarOutline(true);
        renderer.setBaseOutlinePaint(Color.BLACK);

        renderer.setBaseItemLabelGenerator(new CustomXYItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelPaint(Color.BLACK);
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.INSIDE6, TextAnchor.BOTTOM_CENTER));

        XYPlot plot = new XYPlot(dataset, new SymbolAxis("Machines", machines), new NumberAxis("Time"), renderer);

        plot.getDomainAxis().setInverted(true);
        plot.setDomainGridlinesVisible(false);
        plot.setOrientation(PlotOrientation.HORIZONTAL);

        JFreeChart chart = new JFreeChart(plot);
        getContentPane().add(new ChartPanel(chart));
        pack();
        setVisible(true);
    }

//    public void plotPSOSchedule(pso.Particle particle) {
////        System.out.println("\nPlotting...");
//        dataset.removeAllSeries();
//        XYIntervalSeries[] series = new XYIntervalSeries[JSP.numOfJobs];
//        for (int i = 0; i < JSP.numOfJobs; i++) {
////            System.out.println(jobs[i]);
//            series[i] = new XYIntervalSeries(jobs[i]);
//            for (int j = 0; j < JSP.numOfMachines; j++) {
//                Operation op = JSP.jobs[i][j];
//                double startTime = particle.operationStartTimes[i][j];
//                series[i].add(op.machine, op.machine - 0.35, op.machine + 0.35,
//                        startTime, startTime, startTime + op.duration);
////                System.out.println(op.machine + " " + startTime + " " + (startTime + op.duration));
//            }
//        }
//        for(int i = 0; i < JSP.numOfJobs; i++){
//            dataset.addSeries(series[i]);
//        }
//    }

    public void plotPSOSolution(pso.Solution solution) {
//        System.out.println("\nPlotting...");
        dataset.removeAllSeries();
        XYIntervalSeries[] series = new XYIntervalSeries[JSP.numOfJobs];
        for (int i = 0; i < JSP.numOfJobs; i++) {
//            System.out.println(jobs[i]);
            series[i] = new XYIntervalSeries(jobs[i]);
            for (int j = 0; j < JSP.numOfMachines; j++) {
                Operation op = JSP.jobs[i][j];
                double startTime = solution.operationStartTimes[i][j];
                String label = "(" + (j+1) + "/" + (op.job+1) + ")";
                CustomXYIntervalDataItem item =  new CustomXYIntervalDataItem(op.machine, op.machine - 0.35, op.machine + 0.35,
                        startTime, startTime, startTime + op.duration, label);
                series[i].add(item, false);
//                System.out.println(op.machine + " " + startTime + " " + (startTime + op.duration));
            }
        }
        for(int i = 0; i < JSP.numOfJobs; i++){
            dataset.addSeries(series[i]);
        }
    }

    public void plotACOSolution(aco.Solution solution) {
//        System.out.println("\nPlotting...");
        dataset.removeAllSeries();
        XYIntervalSeries[] series = new XYIntervalSeries[JSP.numOfJobs];
        for (int i = 0; i < JSP.numOfJobs; i++) {
//            System.out.println(jobs[i]);
            series[i] = new XYIntervalSeries(jobs[i]);
            for (int j = 0; j < JSP.numOfMachines; j++) {
                Operation op = JSP.jobs[i][j];
                double startTime = solution.operationStartTimes[i][j];
                String label = "(" + (j+1) + "/" + (op.job+1) + ")";
                CustomXYIntervalDataItem item =  new CustomXYIntervalDataItem(op.machine, op.machine - 0.35, op.machine + 0.35,
                        startTime, startTime, startTime + op.duration, label);
                series[i].add(item, false);
//                System.out.println(op.machine + " " + startTime + " " + (startTime + op.duration));
            }
        }
        for(int i = 0; i < JSP.numOfJobs; i++){
            dataset.addSeries(series[i]);
        }
    }
}

