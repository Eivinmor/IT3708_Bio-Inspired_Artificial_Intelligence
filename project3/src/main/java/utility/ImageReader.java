package utility;


import representation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageReader {

    private static String filePathRoot = System.getProperty("user.dir") + "\\src\\main\\resources\\input\\";

    public static Grid readImage(int imageId) {
        int imageWidth;
        int imageHeight;
        Pixel[][] pixels;
        Grid grid;
        try{

            BufferedImage image = ImageIO.read(new File(filePathRoot+imageId));
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();

            pixels = new Pixel[imageHeight][imageWidth];

            for (int i = 0; i < imageHeight; i++) {
                for (int j = 0; j < imageWidth; j++) {
                    pixels[i][j] = new Pixel(i, j, new Color(image.getRGB(j, i)));
                }
            }
            grid = new Grid(pixels);
            return grid;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
