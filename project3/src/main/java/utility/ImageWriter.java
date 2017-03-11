package utility;

import ga.Chromosome;
import ga.Segment;
import representation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageWriter {

    private static String filePathRoot = System.getProperty("user.dir") + "\\src\\main\\resources\\output\\";

    public static void writeGridImage(Grid grid){
        try{
            BufferedImage image = new BufferedImage(grid.width, grid.height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < grid.width; i++) {
                for (int j = 0; j < grid.height; j++) {
                    image.setRGB(i, j, grid.pixelArray[i][j].rgb.getRGB());
                }
            }
            File outputFile = new File(filePathRoot + "grid.png");
            ImageIO.write(image, "png", outputFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeChromosomeImageAvgRgb(Chromosome chromosome, int chromosomeId, boolean drawBorder){
        System.out.println("Writing image");
        try{
            BufferedImage image = new BufferedImage(chromosome.grid.width, chromosome.grid.height, BufferedImage.TYPE_INT_RGB);

            for (Segment segment : chromosome.segments) {
                double[] segmentAvgRgb = segment.calculateAverageRgb();
                Color segmentColor = new Color((int) segmentAvgRgb[0], (int) segmentAvgRgb[1], (int) segmentAvgRgb[2]);
                for (Pixel pixel  : segment.pixels) {
                    image.setRGB(pixel.x, pixel.y, segmentColor.getRGB());
                }
            }
            File outputFile = new File(filePathRoot + "chromosome" + chromosomeId + "Avg.png");
            ImageIO.write(image, "png", outputFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeChromosomeImageRandRgb(Chromosome chromosome, int chromosomeId, boolean drawBorder){
        System.out.println("Writing image");
        try{
            BufferedImage image = new BufferedImage(chromosome.grid.width, chromosome.grid.height, BufferedImage.TYPE_INT_RGB);

            for (Segment segment : chromosome.segments) {
                Color segmentColor = new Color((int)(Math.random() * 0x1000000));
                for (Pixel pixel  : segment.pixels) {
                    image.setRGB(pixel.x, pixel.y, segmentColor.getRGB());
                }
            }
            File outputFile = new File(filePathRoot + "chromosome" + chromosomeId + "Random.png");
            ImageIO.write(image, "png", outputFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
