package utility;

import representation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageWriter {

    private static String filePathRoot = System.getProperty("user.dir") + "\\src\\main\\resources\\output\\";

    public static void writeImage(Grid grid){
        try{
            BufferedImage image = new BufferedImage(grid.width, grid.height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < grid.height; i++) {
                for (int j = 0; j < grid.width; j++) {
                    image.setRGB(j, i, grid.pixelArray[i][j].rgb.getRGB());
                }
            }
            File outputFile = new File(filePathRoot+"solution.png");
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
