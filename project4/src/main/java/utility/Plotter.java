package utility;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.PlotOrientation;

import org.jfree.chart.plot.XYPlot;

import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.TextAnchor;

import representation.*;

import java.awt.*;


public class Plotter extends ApplicationFrame {

    // TODO Add (op order), (job id) labels for operation bars

    String[] machines, jobs;
    XYIntervalSeriesCollection dataset;
    XYBarRenderer renderer;
    Color[] colors;

    public Plotter() {

        super("Schedule");

        colors = new Color[JSP.numOfJobs];
        for (int i = 0; i < JSP.numOfJobs; i++) {
            colors[i] = new Color(
                    Tools.random.nextInt(256),
                    Tools.random.nextInt(256),
                    Tools.random.nextInt(256));
        }

        dataset = new XYIntervalSeriesCollection();

        machines = new String[JSP.numOfMachines];
        for(int i = 0; i < JSP.numOfMachines; i++) machines[i] = "M" + (i + 1);

        jobs = new String[JSP.numOfJobs];
        for(int i = 0; i < JSP.numOfJobs; i++) jobs[i] = "J" + (i + 1);

        renderer = new XYBarRenderer();
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

    public void plotSolution(Solution solution) {
        dataset.removeAllSeries();
        XYIntervalSeries[] series = new XYIntervalSeries[JSP.numOfJobs];
        for (int i = 0; i < JSP.numOfJobs; i++) {
            series[i] = new XYIntervalSeries(jobs[i]);
            for (int j = 0; j < JSP.numOfMachines; j++) {
                Operation op = JSP.jobs[i][j];
                double startTime = solution.operationStartTimes[i][j];
                String label = "(" + (j+1) + "/" + (op.job+1) + ")";
                CustomXYIntervalDataItem item =  new CustomXYIntervalDataItem(op.machine, op.machine - 0.35, op.machine + 0.35,
                        startTime, startTime, startTime + op.duration, label);
                series[i].add(item, false);
            }
        }
        for(int i = 0; i < JSP.numOfJobs; i++){
            dataset.addSeries(series[i]);
            renderer.setSeriesPaint(i, colors[i]);
        }
    }
}

