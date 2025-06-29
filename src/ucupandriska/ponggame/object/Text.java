package ucupandriska.ponggame.object;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Text {
    public String text;
    public Font font;
    public double x, y;
    public Color color = Color.WHITE;
    public boolean centered = false;

    public Text(String text, Font font, double x, double y) {
        this.text = text;
        this.font = font;
        this.x = x;
        this.y = y;
    }

    public Text(int text, Font font, double x, double y) {
        this("" + text, font, x, y);
    }

    public void setText(String newText) {
        this.text = newText;
    }

    public void setText(int newText) {
        this.text = String.valueOf(newText);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
    }

    public void draw(Graphics2D g2) {
        g2.setFont(font);
        g2.setColor(color);

        if (centered) {
            FontMetrics fm = g2.getFontMetrics(font);
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();
            float drawX = (float)(x - textWidth / 2.0);
            float drawY = (float)(y + textHeight / 2.0) - 2; // Small visual tweak
            g2.drawString(text, drawX, drawY);
        } else {
            g2.drawString(text, (float)x, (float)y);
        }
    }
}
