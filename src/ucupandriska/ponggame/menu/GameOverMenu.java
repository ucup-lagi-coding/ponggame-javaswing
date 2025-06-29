package ucupandriska.ponggame.menu;

import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import ucupandriska.ponggame.GameWindow;
import ucupandriska.ponggame.object.Text;
import ucupandriska.ponggame.util.Const;
import ucupandriska.ponggame.util.ColorScheme; // Import ColorScheme for the title color

public class GameOverMenu extends AbstractMenu {

    private String winnerName;

    /**
     * Constructor for the game over menu.
     * @param window The GameWindow instance.
     * @param winnerName The name of the player who won (e.g., "Player One").
     */
    public GameOverMenu(GameWindow window, String winnerName) {
        super(window);
        this.winnerName = winnerName;
    }

    @Override
    protected Text createTitle(Font titleFont) {
        // Dynamic title based on the winner
        Text t = new Text(winnerName + " Wins!", titleFont, Const.SCREEN_WIDTH / 2.0, 150);
        t.setCentered(true);
        t.setColor(ColorScheme.TEXT_GAMEOVER_TITLE); // Use a distinct color for game over title
        return t;
    }

    @Override
    protected List<MenuItem> createMenuItems() {
        return Arrays.asList(
            new MenuItem("Main Menu", () -> {
                System.out.println("GameOverMenu: 'Main Menu' clicked. Returning to main menu.");
                close(); // Close this menu
                window.returnToMainMenu(); // This handles resetting the game and showing MainMenu
            }),
            new MenuItem("Exit",      () -> {
                System.out.println("GameOverMenu: 'Exit' clicked. Exiting game.");
                System.exit(0); // Terminate the application
            })
        );
    }
}
