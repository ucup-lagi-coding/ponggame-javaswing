package ucupandriska.ponggame.object; // Or a suitable package for utilities/graphics

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages and draws the visual trail effect for the ball.
 * Stores a history of the ball's positions and renders them with fading transparency.
 */
public class BallTrail {
    private List<Point> trailPoints; // Stores the past positions of the ball
    private final int MAX_TRAIL_LENGTH; // How many trail segments to draw
    private final Color TRAIL_COLOR; // Color of the trail

    /**
     * Constructs a new BallTrail instance.
     *
     * @param maxTrailLength The maximum number of points to keep in the trail.
     * @param trailColor     The color of the trail segments.
     */
    public BallTrail(int maxTrailLength, Color trailColor) {
        this.MAX_TRAIL_LENGTH = maxTrailLength;
        this.TRAIL_COLOR = trailColor;
        this.trailPoints = new ArrayList<>();
    }

    /**
     * Adds a new point (current ball position) to the trail history.
     * If the trail exceeds its maximum length, the oldest point is removed.
     *
     * @param x The x-coordinate of the ball.
     * @param y The y-coordinate of the ball.
     */
    public void addPoint(int x, int y) {
        trailPoints.add(new Point(x, y));
        if (trailPoints.size() > MAX_TRAIL_LENGTH) {
            trailPoints.remove(0); // Remove the oldest point to maintain length
        }
    }

    /**
     * Clears all points from the trail, effectively making it disappear.
     */
    public void clear() {
        trailPoints.clear();
    }

    /**
     * Draws the ball's trail on the given Graphics2D context.
     * Trail segments will fade out and potentially shrink based on their age.
     *
     * @param g2        The Graphics2D context to draw on.
     * @param ballWidth The width of the actual ball (used for sizing trail segments).
     * @param ballHeight The height of the actual ball (used for sizing trail segments).
     */
    public void draw(Graphics2D g2, int ballWidth, int ballHeight) {
        // Store the original composite and color to restore them later,
        // ensuring other drawing operations are unaffected.
        AlphaComposite originalComposite = (AlphaComposite) g2.getComposite();
        Color originalColor = g2.getColor();

        g2.setColor(TRAIL_COLOR);

        // Iterate through the trail points, from oldest to newest.
        // This ensures proper layering for transparency (older points drawn first).
        for (int i = 0; i < trailPoints.size(); i++) {
            Point p = trailPoints.get(i);

            // Calculate alpha (transparency) based on the point's age in the trail.
            // Points closer to the start of the list (older) will be more transparent.
            float alpha = (float) (i + 1) / MAX_TRAIL_LENGTH;
            if (alpha > 1.0f) alpha = 1.0f; // Ensure alpha doesn't exceed 1.0 (fully opaque)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            // Calculate the size of the trail segment.
            // Older points can be made smaller for a tapering effect.
            // Example: starts at 50% of ball size and grows to 100% as it gets newer.
            int trailSegmentWidth = (int) (ballWidth * (0.5 + 0.5 * alpha));
            int trailSegmentHeight = (int) (ballHeight * (0.5 + 0.5 * alpha));
            
            // Draw an oval for the trail segment.
            // Center the trail segment relative to the ball's original position.
            g2.fillOval(p.x + (ballWidth - trailSegmentWidth) / 2,
                        p.y + (ballHeight - trailSegmentHeight) / 2,
                        trailSegmentWidth,
                        trailSegmentHeight);
        }

        // Restore the original drawing composite and color to avoid affecting subsequent draws.
        g2.setComposite(originalComposite);
        g2.setColor(originalColor);
    }
}
