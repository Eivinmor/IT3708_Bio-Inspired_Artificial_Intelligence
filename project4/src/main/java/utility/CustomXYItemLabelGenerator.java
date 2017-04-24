package utility;

import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalSeriesCollection;


public class CustomXYItemLabelGenerator extends StandardXYItemLabelGenerator{

    public String generateLabel(XYDataset dataset, int series, int item) {
        XYIntervalSeriesCollection sc = (XYIntervalSeriesCollection) dataset;
        CustomXYIntervalDataItem di = (CustomXYIntervalDataItem) sc.getSeries(series).getDataItem(item);
        return di.getLabel();
    }



}
