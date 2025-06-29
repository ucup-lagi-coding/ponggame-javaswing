package ucupandriska.ponggame.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;
import ucupandriska.ponggame.GameWindow;
import ucupandriska.ponggame.object.Text;
import ucupandriska.ponggame.util.Const;

public class PauseMenu extends AbstractMenu {

    public PauseMenu(GameWindow window) {
        super(window);
    }

    @Override
    protected Text createTitle(Font titleFont) {
        Text t = new Text("Paused", titleFont, Const.SCREEN_WIDTH / 2.0, 150);
        t.setCentered(true);
        return t;
    }

    @Override
    protected List<MenuItem> createMenuItems() {
        return Arrays.asList(
                new MenuItem("Resume", () -> {
                    System.out.println("PauseMenu: 'Resume' clicked. Closing menu and unpausing game.");
                    close();
                    window.togglePause(); // This should be the only thing for resume
                }),
                new MenuItem("Main Menu", () -> {
                    System.out.println("PauseMenu: 'Main Menu' clicked. Returning to main menu.");
                    close();
                    window.returnToMainMenu(); // This causes the reset
                }),
                new MenuItem("Exit", () -> {
                    System.out.println("PauseMenu: 'Exit' clicked. Exiting game.");
                    System.exit(0);
                }));
    }
}
