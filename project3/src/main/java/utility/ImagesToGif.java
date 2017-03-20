package utility;


import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class ImagesToGif {

    private static String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\output\\";
    private static String name = "animatedGif.gif";

    public static void readImages(){

        Path path = Paths.get(filePath + name);

        try{
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File folder = new File(filePath);
        File[] fileArray = folder.listFiles();

        try{

            BufferedImage[] images = new BufferedImage[fileArray.length];

            for (int i = 0; i < images.length; i++) {
                System.out.println(fileArray[i].getName());
                BufferedImage image = ImageIO.read(fileArray[i]);
                images[i] = image;
            }

            generateGif(images);

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void generateGif(BufferedImage[] images){

        try {
            ImageWriter writer = null;
            Iterator<ImageWriter> itr = ImageIO.getImageWritersByFormatName("gif");
            if(itr.hasNext()) writer = itr.next();
            ImageOutputStream outputStream = ImageIO.createImageOutputStream(new File(filePath + name));
            IIOMetadata metadata = getMetadata(writer, 100, true);

            writer.setOutput(outputStream);
            writer.prepareWriteSequence(null);

            for (BufferedImage img: images)
            {
                IIOImage temp = new IIOImage(img, null, metadata);
                writer.writeToSequence(temp, null);
            }
            writer.endWriteSequence();

        }catch (Exception e){
            e.printStackTrace();
        }

    }



    private static IIOMetadata getMetadata(ImageWriter writer, int delay, boolean loop)
            throws IIOInvalidTreeException
    {

        ImageTypeSpecifier img_type = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_ARGB);
        IIOMetadata metadata = writer.getDefaultImageMetadata(img_type, null);
        String native_format = metadata.getNativeMetadataFormatName();
        IIOMetadataNode node_tree = (IIOMetadataNode)metadata.getAsTree(native_format);

        IIOMetadataNode graphics_node = getNode("GraphicControlExtension", node_tree);
        graphics_node.setAttribute("delayTime", String.valueOf(delay));
        graphics_node.setAttribute("disposalMethod", "none");
        graphics_node.setAttribute("userInputFlag", "FALSE");

        if(loop)
            makeLoopy(node_tree);

        metadata.setFromTree(native_format, node_tree);

        return metadata;
    }

    private static IIOMetadataNode getNode(String node_name, IIOMetadataNode root)
    {
        IIOMetadataNode node = null;

        for (int i = 0; i < root.getLength(); i++)
        {
            if(root.item(i).getNodeName().compareToIgnoreCase(node_name) == 0)
            {
                node = (IIOMetadataNode) root.item(i);
                return node;
            }
        }

        // Append the node with the given name if it doesn't exist
        node = new IIOMetadataNode(node_name);
        root.appendChild(node);

        return node;
    }

    private static void makeLoopy(IIOMetadataNode root)
    {
        IIOMetadataNode app_extensions = getNode("ApplicationExtensions", root);
        IIOMetadataNode app_node = getNode("ApplicationExtension", app_extensions);

        app_node.setAttribute("applicationID", "NETSCAPE");
        app_node.setAttribute("authenticationCode", "2.0");
        app_node.setUserObject(new byte[]{ 0x1, (byte) (0 & 0xFF), (byte) ((0 >> 8) & 0xFF)});

        app_extensions.appendChild(app_node);
        root.appendChild(app_extensions);
    }


    public static void main(String[] args) {
        ImagesToGif.readImages();
    }
}
