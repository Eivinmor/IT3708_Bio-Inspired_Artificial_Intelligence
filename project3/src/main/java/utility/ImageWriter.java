package utility;

import ga.Chromosome;
import ga.Settings;
import ga.nsga2.NSGA2Chromosome;
import representation.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public abstract class ImageWriter {

    private static String filePathRoot = System.getProperty("user.dir") + "\\src\\main\\resources\\output\\";

    public static void writeGridImage(){
        try{
            BufferedImage image = new BufferedImage(Grid.width, Grid.height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < Grid.width; x++) {
                for (int y = 0; y < Grid.height; y++) {
                    image.setRGB(x, y, Grid.pixelArray[x + (y * Grid.width)].getRGB());
                }
            }
            File outputFile = new File(filePathRoot + "actual.png");
            ImageIO.write(image, "png", outputFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeChromosomeImageRandomRgb(Chromosome chromosome, int id){
        if (chromosome.segmentationIsOutdated) chromosome.calculateSegmentation();
//        System.out.println("Writing image " + id);
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
            File outputFile = new File(filePathRoot + "chromosome" + String.format("%05d", id) + "-" + chromosome.numOfSegments + ".png");
            ImageIO.write(image, "png", outputFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeChromosomeImageWithEdges(Chromosome chromosome, int id){
        if (chromosome.segmentationIsOutdated) chromosome.calculateSegmentation();
//        System.out.println("Writing image " + id);
        try{
            BufferedImage image = new BufferedImage(Grid.width, Grid.height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < Grid.width; x++) {
                for (int y = 0; y < Grid.height; y++) {
                    int pixelId = x + (y * Grid.width);
                    if (isEdgePixel(chromosome, pixelId)) image.setRGB(x, y, Color.GREEN.getRGB());
                    else image.setRGB(x, y, Grid.pixelArray[pixelId].getRGB());
                }
            }
            File outputFile = new File(filePathRoot + "chromosome" + String.format("%05d", id) + "-" + chromosome.numOfSegments + ".png");
            ImageIO.write(image, "png", outputFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeChromosomeEdgesBlackOnWhite(Chromosome chromosome, int id){
        if (chromosome.segmentationIsOutdated) chromosome.calculateSegmentation();
//        System.out.println("Writing image " + id);
        try{
            BufferedImage image = new BufferedImage(Grid.width, Grid.height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < Grid.width; x++) {
                for (int y = 0; y < Grid.height; y++) {
                    int pixelId = x + (y * Grid.width);
                    if (isEdgePixel(chromosome, pixelId)) image.setRGB(x, y, Color.BLACK.getRGB());
                    else image.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
            File outputFile = new File(filePathRoot + "chromosome" + String.format("%05d", id) + "-" + chromosome.numOfSegments + ".png");
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

    public static void clearFolder() {
        try{
            File folder = new File(filePathRoot);
            File[] fileArray = folder.listFiles();
            for (int i = 0; i < fileArray.length; i++){
                if (fileArray[i].getName().equals("actual.png")) continue;
                Files.deleteIfExists(Paths.get(filePathRoot + fileArray[i].getName()));
//                System.out.println(fileArray[i].getName());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeAllNSGA2Chromosomes(ArrayList<NSGA2Chromosome> chromosomes) {
        System.out.print("\nWriting images...");
        clearFolder();
        int i = 0;
        for (NSGA2Chromosome c : chromosomes) {
//            ImageWriter.writeChromosomeImageRandomRgb(c, i);
            writeChromosomeImageWithEdges(c, i);
            i++;
        }
        System.out.println(" done");
    }
}
