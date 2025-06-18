package ucupandriska.ponggame.util;

public class GameSettings {
    private static Difficulty difficulty = Difficulty.MEDIUM; // Default

    public static void setDifficulty(Difficulty diff) {
        difficulty = diff;
    }

    public static Difficulty getDifficulty() {
        return difficulty;
    }
}