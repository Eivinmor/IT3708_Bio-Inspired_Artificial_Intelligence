package utility;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;


import org.jfree.chart.plot.XYPlot;

import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import representation.JSP;
import representation.Operation;

import java.awt.*;


public class Plotter extends ApplicationFrame {


    String[] machines, jobs;
    XYIntervalSeriesCollection dataset;

    public Plotter() {

        super("Gantt chart");

        dataset = new XYIntervalSeriesCollection();

        machines = new String[JSP.numOfMachines];
        for(int i = 0; i < JSP.numOfMachines; i++) machines[i] = "M" + i;

        jobs = new String[JSP.numOfJobs];
        for(int i = 0; i < JSP.numOfJobs; i++) jobs[i] = "J" + i;

        //Create series. Start and end times are used as y intervals, and the room is represented by the x value


//
//        for(int k = 0; k < totalCourseCount; k++){
//            //get a random room
//            int currentRoom = r.nextInt(JSP.numOfMachines);
//            //get a random course
//            int currentCourse = r.nextInt(JSP.numOfJobs);
//            //get a random course duration (1-3 h)
//            int time = r.nextInt(3) + 1;
//            //Encode the room as x value. The width of the bar is only 0.6 to leave a small gap. The course starts 0.1 h/6 min after the end of the preceding course.
//            series[currentCourse].add(currentRoom, currentRoom - 0.3, currentRoom + 0.3, startTimes[currentRoom], startTimes[currentRoom] +0.1, startTimes[currentRoom] + time - 0.1);
//            //Increase start time for the current room
//            startTimes[currentRoom] += time;
//        }
        XYBarRenderer renderer = new XYBarRenderer();
        renderer.setUseYInterval(true);
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardXYBarPainter());
        renderer.setDrawBarOutline(true);
        renderer.setBaseOutlinePaint(Color.BLACK);

        XYPlot plot = new XYPlot(dataset, new SymbolAxis("Machines", machines), new NumberAxis(), renderer);

        plot.getDomainAxis().setInverted(true);
        plot.setDomainGridlinesVisible(false);
        plot.setOrientation(PlotOrientation.HORIZONTAL);

        JFreeChart chart = new JFreeChart(plot);
        getContentPane().add(new ChartPanel(chart));
        pack();
        setVisible(true);
    }

    public void plotPSOSchedule(pso.Particle particle) {
//        System.out.println("\nPlotting...");
        dataset.removeAllSeries();
        XYIntervalSeries[] series = new XYIntervalSeries[JSP.numOfJobs];
        for (int i = 0; i < JSP.numOfJobs; i++) {
//            System.out.println(jobs[i]);
            series[i] = new XYIntervalSeries(jobs[i]);
            for (int j = 0; j < JSP.numOfMachines; j++) {
                Operation op = JSP.jobs[i][j];
                double startTime = particle.operationStartTimes[i][j];
                series[i].add(op.machine, op.machine - 0.35, op.machine + 0.35,
                        startTime, startTime, startTime + op.duration);
//                System.out.println(op.machine + " " + startTime + " " + (startTime + op.duration));
            }
        }
        for(int i = 0; i < JSP.numOfJobs; i++){
            dataset.addSeries(series[i]);
        }
    }
}