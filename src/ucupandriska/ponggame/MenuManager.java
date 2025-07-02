package ucupandriska.ponggame;

import ucupandriska.ponggame.menu.*;

public class MenuManager {
    private final GameWindow window;
    private PauseMenu pauseMenu;

    public MenuManager(GameWindow window) {
        this.window = window;
    }

    public void showPauseMenu() {
        pauseMenu = new PauseMenu(window);
        pauseMenu.show();
    }

    public void showMainMenu() {
        new MainMenu(window).show();
    }

    public void showGameOverMenu(String winnerName) {
        GameOverMenu gameOverMenu = new GameOverMenu(window, winnerName);
        gameOverMenu.show();
    }
}