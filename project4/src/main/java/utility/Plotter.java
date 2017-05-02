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

        colors = getPredefinedColors();

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

    private Color[] getPredefinedColors() {
        Color[] colors = {
                new Color (255, 0, 86),
                new Color (14, 76, 161),
//                new Color (255, 229, 2),
                new Color (255, 219, 102),
                new Color (0, 95, 57),
                new Color (158, 0, 142),
                new Color (0, 185, 23),
                new Color (255, 147, 126),
                new Color (164, 36, 0),
                new Color (145, 208, 203),
                new Color (107, 104, 130),
//                new Color (0, 0, 255),
                new Color (0, 125, 181),
                new Color (254, 137, 0),
                new Color (106, 130, 108),
                new Color (0, 174, 126),
                new Color (194, 140, 159),
                new Color (190, 153, 112),
//                new Color (0, 143, 156),
                new Color (95, 173, 78),
//                new Color (255, 0, 0),
//                new Color (255, 0, 246),
                new Color (104, 61, 59),
                new Color (255, 116, 163),
                new Color (150, 138, 232),
                new Color (152, 255, 82),
                new Color (167, 87, 64),
                new Color (1, 255, 254),
                new Color (255, 238, 232),
                new Color (189, 198, 255),
                new Color (1, 208, 255),
//                new Color (187, 136, 0),
                new Color (165, 255, 210),
                new Color (255, 166, 254),
//                new Color (119, 77, 0),
                new Color (122, 71, 130),
//                new Color (38, 52, 0),
                new Color (0, 71, 84),
                new Color (255, 177, 103),
                new Color (144, 251, 146),
                new Color (126, 45, 210),
                new Color (189, 211, 147),
                new Color (229, 111, 254),
                new Color (222, 255, 116),
                new Color (0, 255, 120),
                new Color (0, 155, 255),
                new Color (0, 100, 1),
                new Color (0, 118, 255),
                new Color (133, 169, 0),

                new Color (120, 130, 49),
                new Color (0, 255, 198),
                new Color (255, 110, 65),
                new Color (232, 94, 190)};
        return colors;
    }

    private Color[] generateRandomColors(){
        Color[] newColors = new Color[JSP.numOfJobs];
        int min = 180;
        for (int i = 0; i < JSP.numOfJobs; i++) {
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

