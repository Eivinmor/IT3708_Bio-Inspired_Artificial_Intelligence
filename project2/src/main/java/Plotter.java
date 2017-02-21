
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;


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


        // DOMAIN AND RANGE
        ValueAxis domain = new NumberAxis();
        ValueAxis range = new NumberAxis();
        domain.setVisible(false);
        range.setVisible(false);
        plot.setDomainAxis(0, domain);
        plot.setRangeAxis(0, range);


        // SCATTER PLOT
        XYItemRenderer dotRenderer = new XYLineAndShapeRenderer(false, true);
//        dotRenderer.setSeriesShape(0, new Ellipse2D.Double(-5.0, -5.0, 10.0, 10.0));
        dotRenderer.setSeriesShape(0, new Rectangle2D.Double(-7.0, -7.0, 14.0, 14.0));
        dotRenderer.setSeriesPaint(0, Color.BLACK);
        dotRenderer.setSeriesShape(1, new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
        dotRenderer.setSeriesPaint(1, Color.GRAY);
        plot.setDataset(0, depotsAndCustomersCollection);
        plot.setRenderer(0, dotRenderer);
        plot.mapDatasetToDomainAxis(0, 0);
        plot.mapDatasetToRangeAxis(0, 0);


        // LINES PLOT
        XYItemRenderer lineRenderer = new XYLineAndShapeRenderer(true, false);
        ((AbstractRenderer)lineRenderer).setAutoPopulateSeriesStroke(false);
        lineRenderer.setBaseStroke(new BasicStroke(2));
        plot.setDataset(1, routeSequenceCollection);
        plot.setRenderer(1, lineRenderer);
        plot.mapDatasetToDomainAxis(1, 0);
        plot.mapDatasetToRangeAxis(1, 0);


        // DISPLAY AND STYLING
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);

        JFreeChart chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
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

    public void addScatterSeries(String key, int[][] coordinates) {
        XYSeries newSeries = new XYSeries(key, false, true);
        for (int i = 0; i < coordinates.length; i++) {
            newSeries.add(coordinates[i][0], coordinates[i][1]);
        }
        depotsAndCustomersCollection.addSeries(newSeries);
    }

    public void addLineSeries(String key, int[][] coordinates){
        XYSeries newSeries = new XYSeries(key, false, true);
        for (int i = 0; i < coordinates.length; i++) {
            newSeries.add(coordinates[i][0], coordinates[i][1]);
        }
        routeSequenceCollection.addSeries(newSeries);
    }
}