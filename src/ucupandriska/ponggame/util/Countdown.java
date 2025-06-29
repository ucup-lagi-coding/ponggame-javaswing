package ucupandriska.ponggame.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.FontMetrics;

import ucupandriska.ponggame.object.Text;

public class Countdown {
    private double duration;
    private double remaining;
    private boolean active;

    private Font countdownFont = FontLoader.loadFont(Const.FONT_PATH, (float)Const.COUNTFOWN_FONT_SIZE);
    private final Text countdownText = new Text("", countdownFont, 0, 0);

    public Countdown(double duration) {
        this.duration = duration;
        this.remaining = 0;
        this.active = false;
    }

    public void start() {
        this.remaining = duration;
        this.active = true;
    }

    public void update(double dt) {
        if (!active) return;

        remaining -= dt;
        if (remaining <= 0) {
            active = false;
        }
    }

    public boolean isActive() {
        return active;
    }

    public int getCountdownSeconds() {
        return (int) Math.ceil(remaining);
    }

    public void draw(Graphics2D g2) {
        if (!active) return;

        int seconds = getCountdownSeconds();
        if (seconds <= 0) return;

        // Update text content
        countdownText.text = String.valueOf(seconds);

        // Center position
        FontMetrics fm = g2.getFontMetrics(countdownFont);
        int textWidth = fm.stringWidth(countdownText.text);
        int textHeight = fm.getAscent();

        countdownText.x = Const.SCREEN_WIDTH / 2.0 - textWidth / 2.0;
        countdownText.y = Const.SCREEN_HEIGHT / 2.0 + textHeight / 2.0;

        countdownText.draw(g2);
    }
}
