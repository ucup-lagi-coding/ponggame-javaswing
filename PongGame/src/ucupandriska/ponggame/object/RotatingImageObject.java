package ucupandriska.ponggame.object;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

public class RotatingImageObject {
    public double x, y, width, height;
    public double rotationAngle = 0;
    public Image image;

    public RotatingImageObject(double x, double y, double width, double height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public void rotate(double amount) {
        rotationAngle += amount;
        // Keep rotation angle within 0–2π to prevent overflow
        if (rotationAngle > Math.PI * 2) rotationAngle -= Math.PI * 2;
        if (rotationAngle < 0) rotationAngle += Math.PI * 2;
    }

    public void draw(Graphics2D g2) {
        AffineTransform old = g2.getTransform();

        AffineTransform transform = new AffineTransform();
        transform.translate(x + width / 2, y + height / 2);
        transform.rotate(rotationAngle);
        transform.translate(-width / 2, -height / 2);

        g2.drawImage(image, transform, null);
        g2.setTransform(old);
    }
}
