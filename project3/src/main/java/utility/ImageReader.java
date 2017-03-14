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
        Color[] pixels;
        try{
            BufferedImage image = ImageIO.read(new File(filePathRoot+imageId+"\\Test image.jpg"));
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();

            pixels = new Color[imageWidth*imageHeight];
            for (int x = 0; x < imageWidth; x++) {
                for (int y = 0; y < imageHeight; y++) {
                    pixels[x + (y * imageWidth)] = new Color(image.getRGB(x, y));
                }
            }
            Grid.pixelArray = pixels;
            Grid.width = imageWidth;
            Grid.height = imageHeight;
//            Grid.pixelNeighbourDistances = Grid.calculatePixelNeighbourDistances();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
