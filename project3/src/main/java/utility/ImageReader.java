package utility;


import representation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageReader {

    private static String filePathRoot = System.getProperty("user.dir") + "\\src\\main\\resources\\input\\";

    public static void readImage(int imageId) {
        int imageWidth;
        int imageHeight;
        Pixel[][] pixels;
        try{
            BufferedImage image = ImageIO.read(new File(filePathRoot+imageId+"\\Test image.jpg"));
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();

            pixels = new Pixel[imageWidth][imageHeight];

            for (int i = 0; i < imageWidth; i++) {
                for (int j = 0; j < imageHeight; j++) {
                    pixels[i][j] = new Pixel(i, j, new Color(image.getRGB(i, j)));
                }
            }
            Grid.pixelArray = pixels;
            Grid.width = imageWidth;
            Grid.height = imageHeight;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
