package common;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.util.ShapeUtilities;

import java.awt.*;


public class Plotter {

    private XYSeriesCollection dataset;
    private XYSeries agentScoreData;
    private String applicationTitle, chartTitle, xAxisTitle, yAxisTitle;

    public Plotter(String chartTitle, String xAxisTitle, String yAxisTitle){
        this.applicationTitle = chartTitle;
        this.chartTitle = chartTitle;
        this.xAxisTitle = xAxisTitle;
        this.yAxisTitle = yAxisTitle;
        agentScoreData = new XYSeries("");
        dataset = new XYSeriesCollection();
        dataset.addSeries(agentScoreData);
    }

    public void plot(){
        ApplicationFrame applicationFrame = new ApplicationFrame(applicationTitle);
        JFreeChart scatterChart = ChartFactory.createScatterPlot(
                chartTitle,
                xAxisTitle,
                yAxisTitle,
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false);
        XYPlot plot = scatterChart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.getRangeAxis().setLowerMargin(0.1);
        plot.getRangeAxis().setUpperMargin(0.1);
        plot.getDomainAxis().setLowerMargin(0.1);
        plot.getDomainAxis().setUpperMargin(0.1);

        Shape cross = ShapeUtilities.createDiagonalCross(3, (float)0.2);
        plot.getRenderer().setSeriesShape(0, cross);
        plot.getRenderer().setSeriesPaint(0, Color.RED);

        ChartPanel chartPanel = new ChartPanel(scatterChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 600));
        applicationFrame.setContentPane(chartPanel);
        applicationFrame.pack();
        applicationFrame.setVisible(true);
    }

    public void addData(int x, double y){
        agentScoreData.add(x, y);
    }
}