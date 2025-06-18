package ucupandriska.ponggame.util;

import java.awt.Color;

public class ColorScheme {
    // Background
    public static final Color BACKGROUND = hex("#343131");         // Dark gray
    public static final Color MENU_BACKGROUND = hex("#343131");         // Dark gray

    // Text
    public static final Color TEXT_PRIMARY = hex("#EEDF7A");        // Soft yellow
    public static final Color TEXT_HIGHLIGHT = hex("#D8A25E");      // Muted orange
    public static final Color TEXT_GAMEOVER_TITLE = hex("#4fab66");
    public static final Color TEXT_ACTIVE_DIFFICULTY = hex("#7d706f");

    // Game objects
    public static final Color PADDLE = hex("#A04747");              // Warm red
    public static final Color BALL = hex("#EEDF7A");                // Soft yellow
    public static final Color SCORE = hex("#D8A25E");               // Muted orange

    // UI / FX
    public static final Color OUTLINE = hex("#A04747");             // Red tone
    public static final Color SHADOW = new Color(0, 0, 0, 100);     // Transparent black

    private static Color hex(String hexCode) {
        return Color.decode(hexCode);
    }
}
