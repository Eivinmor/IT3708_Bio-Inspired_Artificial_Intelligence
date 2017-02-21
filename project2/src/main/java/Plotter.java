
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.awt.geom.Ellipse2D;


public class Plotter {

    private String chartTitle;
    private XYSeriesCollection depotsAndCustomersCollection, routeSequenceCollection;

    public Plotter(String chartTitle){
        this.chartTitle = chartTitle;
        depotsAndCustomersCollection = new XYSeriesCollection();
        routeSequenceCollection = new XYSeriesCollection();
    }

    public void plot(){
        ApplicationFrame applicationFrame = new ApplicationFrame(chartTitle);

        XYPlot plot = new XYPlot();

        // SCATTER
        XYItemRenderer dotRenderer = new XYLineAndShapeRenderer(false, true);
        dotRenderer.setSeriesShape(0, new Ellipse2D.Double(-4.0, -4.0, 8.0, 8.0));
        dotRenderer.setSeriesPaint(0, Color.BLACK);
        dotRenderer.setSeriesShape(1, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
        dotRenderer.setSeriesPaint(1, Color.GRAY);
        ValueAxis domain = new NumberAxis();
        domain.setVisible(false);
        ValueAxis range = new NumberAxis();
        range.setVisible(false);

        plot.setDataset(0, depotsAndCustomersCollection);
        plot.setRenderer(0, dotRenderer);
        plot.setDomainAxis(0, domain);
        plot.setRangeAxis(0, range);

        plot.mapDatasetToDomainAxis(0, 0);
        plot.mapDatasetToRangeAxis(0, 0);


        // LINES
        XYItemRenderer lineRenderer = new XYLineAndShapeRenderer(true, false);

        plot.setDataset(1, routeSequenceCollection);
        plot.setRenderer(1, lineRenderer);
        plot.setDomainAxis(1, domain);
        plot.setRangeAxis(1, range);

        plot.mapDatasetToDomainAxis(1, 1);
        plot.mapDatasetToRangeAxis(1, 1);


        JFreeChart chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        plot.setBackgroundPaint(Color.WHITE);

        plot.setRangeZeroBaselineVisible(true);
        plot.setRangeZeroBaselineStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[] {6.0f, 6.0f}, 0.0f));
        plot.setRangeZeroBaselinePaint(Color.GRAY);

        TextTitle newTitle = new TextTitle(chartTitle, new Font("SansSerif", Font.BOLD, 16));
        newTitle.setPaint(Color.DARK_GRAY);
        chart.setTitle(newTitle);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1024, 576));
        chartPanel.setMaximumDrawHeight(1080);
        chartPanel.setMaximumDrawWidth(1920);
        applicationFrame.setContentPane(chartPanel);
        applicationFrame.pack();
        applicationFrame.setVisible(true);
        RefineryUtilities.positionFrameOnScreen(applicationFrame, 0.0, 0.92);
    }

    public void addDepotsSeries(int[][] coordinates) {
        XYSeries newSeries = new XYSeries("Depots", false, true);
        for (int i = 0; i < coordinates.length; i++) {
            newSeries.add(coordinates[i][0], coordinates[i][1]);
        }
        depotsAndCustomersCollection.addSeries(newSeries);
    }

    public void addCustomersSeries(int[][] coordinates) {
        XYSeries newSeries = new XYSeries("Customers", false, true);
        for (int i = 0; i < coordinates.length; i++) {
            newSeries.add(coordinates[i][0], coordinates[i][1]);
        }
        depotsAndCustomersCollection.addSeries(newSeries);
    }

    public void addRouteSeries(String name, int[][] coordinates){
        XYSeries newSeries = new XYSeries(name, false, true);
        for (int i = 0; i < coordinates.length; i++) {
            newSeries.add(coordinates[i][0], coordinates[i][1]);
        }
        routeSequenceCollection.addSeries(newSeries);
    }
}