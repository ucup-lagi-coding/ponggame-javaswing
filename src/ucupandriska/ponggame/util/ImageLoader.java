package ucupandriska.ponggame.util;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageLoader {

    /**
     * Loads an image from the resource path.
     * @param path The resource path, e.g. "/resource/images/ball.png"
     * @return The loaded Image, or null if failed.
     */
    public static Image loadImage(String path) {
        try {
            return ImageIO.read(ImageLoader.class.getResourceAsStream(path));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error loading image: " + path);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads and scales an image from the resource path.
     * @param path The resource path.
     * @param width The desired width.
     * @param height The desired height.
     * @return The scaled Image, or null if failed.
     */
    public static Image loadAndScaleImage(String path, int width, int height) {
        Image image = loadImage(path);
        if (image != null) {
            return scaleImage(image, width, height);
        }
        return null;
    }

    /**
     * Scales an existing image to the given width and height.
     * @param img The original image.
     * @param width The desired width.
     * @param height The desired height.
     * @return A new scaled Image.
     */
    public static Image scaleImage(Image img, int width, int height) {
        return img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
}
