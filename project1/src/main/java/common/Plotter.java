package common;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


public class Plotter {

    private DefaultCategoryDataset dataset;
    private String windowTitle, chartTitle, xAxisTitle, yAxisTitle;

    public Plotter(String windowTitle, String chartTitle, String xAxisTitle, String yAxisTitle){
        dataset = new DefaultCategoryDataset();
        this.windowTitle = windowTitle;
        this.chartTitle = chartTitle;
        this.xAxisTitle = xAxisTitle;
        this.yAxisTitle = yAxisTitle;
    }

    public void plot(){
        ApplicationFrame applicationFrame = new ApplicationFrame(windowTitle);
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                xAxisTitle,
                yAxisTitle,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367));
        applicationFrame.setContentPane(chartPanel);
        applicationFrame.pack();
        applicationFrame.setVisible(true);
    }

    public void addData(double value, String rowKey, String columnKey){
        dataset.addValue(value, rowKey, columnKey);
    }

//    private DefaultCategoryDataset createDataset(){
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
//        dataset.addValue( 15 , "schools" , "1970" );
//        dataset.addValue( 30 , "schools" , "1980" );
//        dataset.addValue( 60 , "schools" ,  "1990" );
//        dataset.addValue( 120 , "schools" , "2000" );
//        dataset.addValue( 240 , "schools" , "2010" );
//        dataset.addValue( 300 , "schools" , "2014" );
//        return dataset;
//    }
}