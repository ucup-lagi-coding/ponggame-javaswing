package ucupandriska.ponggame.util;

import java.util.Locale;
import java.util.Objects;

public class MenuInfoHelper {
    public static String getInfoForMenuItem(String label) {
        if (label == null)
            return "";
        label = label.toLowerCase(Locale.ROOT);
        if (label.contains("start")) {
            return "Multiplayer or Human vs Bot";
        }
        if (label.contains("multiplayer")) {
            return "Player 1: W/S  |  Player 2: Up/Down";
        }
        if (label.contains("bot")) {
            return "You: W/S  |  Bot: Auto";
        }
        if (label.contains("difficulty")) {
            return "Choose ball and paddle speed";
        }
        if (label.contains("resume")) {
            return "Press Enter or Space to resume";
        }
        if (label.contains("main menu")) {
            return "Return to the main menu";
        }
        if (label.contains("exit")) {
            return "Exit the game";
        }
        return "";
    }
}