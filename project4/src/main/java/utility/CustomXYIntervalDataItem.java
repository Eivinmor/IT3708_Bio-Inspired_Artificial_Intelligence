package utility;

import org.jfree.data.xy.XYIntervalDataItem;

public class CustomXYIntervalDataItem extends XYIntervalDataItem {

    private String label;

    public CustomXYIntervalDataItem(double x, double xLow, double xHigh, double y, double yLow, double yHigh, String label) {
        super(x, xLow, xHigh, y, yLow, yHigh);
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
