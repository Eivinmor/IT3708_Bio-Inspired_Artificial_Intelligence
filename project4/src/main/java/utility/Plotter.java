package utility;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.PlotOrientation;

import org.jfree.chart.plot.XYPlot;

import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.TextAnchor;

import representation.*;

import java.awt.*;


public class Plotter extends ApplicationFrame {

    private String[] machines, jobs;
    private XYIntervalSeriesCollection dataset;
    private XYBarRenderer renderer;
    private Color[] colors;

    public Plotter() {

        super("Schedule");

        colors = generateColors(JSP.numOfJobs);

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
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1024, 576));
        chartPanel.setMaximumDrawHeight(1080);
        chartPanel.setMaximumDrawWidth(1920);
        getContentPane().add(chartPanel);
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

    private Color[] generateColors(int amount){
        Color[] newColors = new Color[amount];
        int min = 180;
        for (int i = 0; i < amount; i++) {
            int r = Tools.random.nextInt(256);
            int g = Tools.random.nextInt(256);
            int b = Tools.random.nextInt(256);
            while (r + g + b < min) {
                r = Tools.random.nextInt(256);
                g = Tools.random.nextInt(256);
                b = Tools.random.nextInt(256);
            }
            newColors[i] = new Color(r, g, b);
        }
        return newColors;
    }
}

