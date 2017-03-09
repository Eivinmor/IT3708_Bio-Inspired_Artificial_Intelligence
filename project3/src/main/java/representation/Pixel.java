package representation;

import java.awt.*;


public class Pixel {

    public final int x, y;
    public final Color rgb;

    public Pixel(int y, int x, Color rgb) {
        this.x = x;
        this.y = y;
        this.rgb = rgb;
    }
}
