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
        Grid grid = null;
        try{
            BufferedImage image = ImageIO.read(new File(filePathRoot+imageId+"\\Test image.jpg"));
            imageWidth = image.getWidth()-1;
            imageHeight = image.getHeight()-1;

            pixels = new Pixel[imageHeight][imageWidth];

            for (int i = 0; i < imageHeight; i++) {
                for (int j = 0; j < imageWidth; j++) {
                    pixels[i][j] = new Pixel(i, j, new Color(image.getRGB(j, i)));
                }
            }
            grid = new Grid(pixels);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return grid;
    }
}
