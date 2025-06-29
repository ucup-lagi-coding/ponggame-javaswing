package ucupandriska.ponggame.menu;

import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import ucupandriska.ponggame.GameWindow;
import ucupandriska.ponggame.object.Text;
import ucupandriska.ponggame.util.Const;
import ucupandriska.ponggame.util.GameSettings;
import ucupandriska.ponggame.util.GameSettings.GameMode;

public class ModeMenu extends AbstractMenu {
    public ModeMenu(GameWindow window) {
        super(window);
    }

    @Override
    protected Text createTitle(Font titleFont) {
        Text t = new Text("Select Mode", titleFont, Const.SCREEN_WIDTH / 2.0, 150);
        t.setCentered(true);
        return t;
    }

    @Override
    protected List<MenuItem> createMenuItems() {
        return Arrays.asList(
            new MenuItem("Multiplayer", () -> {
                GameSettings.setGameMode(GameMode.MULTIPLAYER);
                window.startGame();
                close();
            }),
            new MenuItem("Play vs Bot", () -> {
                GameSettings.setGameMode(GameMode.BOT);
                window.startGame();
                close();
            }),
            new MenuItem("Back", () -> {
                close();
                new MainMenu(window).show(); // <-- Add this line to re-show main menu
            })
        );
    }
}