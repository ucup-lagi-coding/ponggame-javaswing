package ucupandriska.ponggame.util;

public class GameSettings {
    private static Difficulty difficulty = Difficulty.MEDIUM; // Default
    private static GameMode gameMode = GameMode.MULTIPLAYER; // Default

    public static void setDifficulty(Difficulty diff) {
        difficulty = diff;
    }

    public static Difficulty getDifficulty() {
        return difficulty;
    }

    public enum GameMode {
        MULTIPLAYER,
        BOT
    }

    public static void setGameMode(GameMode mode) {
        gameMode = mode;
    }

    public static GameMode getGameMode() {
        return gameMode;
    }
}