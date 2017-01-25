package common;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;

import java.awt.*;


public class Plotter {

    private XYSeriesCollection dataset;
    private XYSeries agentScoreData;
    private String chartTitle, xAxisTitle, yAxisTitle;
    private int numOfXValues;

    public Plotter(String chartTitle, String xAxisTitle, String yAxisTitle, int numOfXValues){
        this.chartTitle = chartTitle;
        this.xAxisTitle = xAxisTitle;
        this.yAxisTitle = yAxisTitle;
        this.numOfXValues = numOfXValues;
        agentScoreData = new XYSeries("agent");
        dataset = new XYSeriesCollection();
        dataset.addSeries(agentScoreData);
    }

    public void plot(){
        ApplicationFrame applicationFrame = new ApplicationFrame(chartTitle);
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

        NumberAxis rangeAxis = new NumberAxis(yAxisTitle);
        rangeAxis.setLowerMargin(0.1);
        rangeAxis.setUpperMargin(0.1);
        rangeAxis.setUpperBound(50);
        rangeAxis.setLowerBound(-100);
        rangeAxis.setTickUnit(new NumberTickUnit(10));
        rangeAxis.setLabelFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        rangeAxis.setLabelPaint(Color.DARK_GRAY);
        plot.setRangeAxis(rangeAxis);

        NumberAxis domainAxis = new NumberAxis(xAxisTitle);
        domainAxis.setLowerBound(-numOfXValues*0.049);    // Ugly workaround
        domainAxis.setUpperBound(numOfXValues*1.049);
        domainAxis.setLabelFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        domainAxis.setLabelPaint(Color.DARK_GRAY);
        plot.setDomainAxis(domainAxis);

        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        Shape cross = ShapeUtilities.createDiagonalCross(3, (float)0.2);
        plot.getRenderer().setSeriesShape(0, cross);
        plot.getRenderer().setSeriesPaint(0, Color.RED);

        plot.setRangeZeroBaselineVisible(true);
        plot.setRangeZeroBaselineStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[] {6.0f, 6.0f}, 0.0f));
        plot.setRangeZeroBaselinePaint(Color.GRAY);

        TextTitle newTitle = new TextTitle(chartTitle, new Font("SansSerif", Font.BOLD, 16));
        newTitle.setPaint(Color.DARK_GRAY);
        scatterChart.setTitle(newTitle);


        ChartPanel chartPanel = new ChartPanel(scatterChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1024, 576));
        chartPanel.setMaximumDrawHeight(1080);
        chartPanel.setMaximumDrawWidth(1920);
        applicationFrame.setContentPane(chartPanel);
        applicationFrame.pack();
        applicationFrame.setVisible(true);
        RefineryUtilities.positionFrameOnScreen(applicationFrame, 0.0, 0.92);
    }

    public void addData(int x, double y){
        agentScoreData.add(x, y);
    }
}