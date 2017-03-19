package utility;

import ga.Chromosome;
import ga.Settings;
import representation.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageWriter {

    private static String filePathRoot = System.getProperty("user.dir") + "\\src\\main\\resources\\output\\";

    public static void writeGridImage(){
        try{
            BufferedImage image = new BufferedImage(Grid.width, Grid.height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < Grid.width; x++) {
                for (int y = 0; y < Grid.height; y++) {
                    image.setRGB(x, y, Grid.pixelArray[x + (y * Grid.width)].getRGB());
                }
            }
            File outputFile = new File(filePathRoot + "grid.png");
            ImageIO.write(image, "png", outputFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeChromosomeImageRandomRgb(Chromosome chromosome, int chromosomeId){
        if (chromosome.segmentationIsOutdated) chromosome.calculateSegmentation();
        System.out.println("Writing image");
        try{
            Color[] segmentColors = new Color[chromosome.numOfSegments];

            for (int i = 0; i < chromosome.numOfSegments; i++) {
                segmentColors[i] = new Color((int)(Math.random() * 0x1000000));
            }
            BufferedImage image = new BufferedImage(Grid.width, Grid.height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < Grid.width; x++) {
                for (int y = 0; y < Grid.height; y++) {
                    int pixelId = x + (y * Grid.width);
                    if (Settings.drawBorders && isEdgePixel(chromosome, pixelId)) image.setRGB(x, y, Color.WHITE.getRGB());
                    else image.setRGB(x, y, segmentColors[chromosome.segmentation[pixelId]].getRGB());
                }
            }
            File outputFile = new File(filePathRoot + "chromosome" + chromosomeId + "Avg.png");
            ImageIO.write(image, "png", outputFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isEdgePixel(Chromosome chromosome, int pixelId) {
        for (int nb : Grid.getNeighbourPixels(pixelId))
            if (chromosome.segmentation[pixelId] != chromosome.segmentation[nb]) return true;
        return false;
    }
//
//    public static void writeChromosomeImageRandRgb(Chromosome chromosome, int chromosomeId, boolean drawBorder){
//        System.out.println("Writing image");
//        try{
//            BufferedImage image = new BufferedImage(Grid.width, Grid.height, BufferedImage.TYPE_INT_RGB);
//
//            for (Segment segment : chromosome.segments) {
//                Color segmentColor = new Color((int)(Math.random() * 0x1000000));
//                for (Pixel pixel  : segment.pixels) {
//                    image.setRGB(pixel.x, pixel.y, segmentColor.getRGB());
//                }
//            }
//            File outputFile = new File(filePathRoot + "chromosome" + chromosomeId + "Random.png");
//            ImageIO.write(image, "png", outputFile);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
