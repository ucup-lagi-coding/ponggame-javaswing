package ucupandriska.ponggame.menu;

import ucupandriska.ponggame.GameWindow;
import ucupandriska.ponggame.object.Text;
import ucupandriska.ponggame.util.Const;

import java.awt.Font;
import java.util.Arrays;
import java.util.List;

public class MainMenu extends AbstractMenu {

    public MainMenu(GameWindow window) {
        super(window);
    }

    @Override
    protected Text createTitle(Font titleFont) {
        Text t = new Text("Pong Game", titleFont, Const.SCREEN_WIDTH/2.0, 150);
        t.setCentered(true);
        return t;
    }

    @Override
    protected List<MenuItem> createMenuItems() {
        return Arrays.asList(
            new MenuItem("Start", () -> {
                new ModeMenu(window).show();
                close();
            }),
            new MenuItem("Difficulty", () -> {
                new DifficultyMenu(window).show();
            }),
            new MenuItem("Exit", () -> System.exit(0))
        );
    }
}
