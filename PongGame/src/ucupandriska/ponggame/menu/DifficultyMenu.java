package ucupandriska.ponggame.menu;

import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import ucupandriska.ponggame.GameWindow;
import ucupandriska.ponggame.object.Text;
import ucupandriska.ponggame.util.Const;
import ucupandriska.ponggame.util.Difficulty;
import ucupandriska.ponggame.util.FontLoader;
import ucupandriska.ponggame.util.GameSettings;

public class DifficultyMenu extends AbstractMenu {

    public DifficultyMenu(GameWindow window) {
        super(window); // Call superclass constructor first
    }

    @Override
    protected Text createTitle(Font titleFont) {
        Text t = new Text("Select Difficulty", titleFont, Const.SCREEN_WIDTH / 2.0, 150);
        t.setCentered(true);
        return t;
    }

    @Override
    protected List<MenuItem> createMenuItems() {
        return Arrays.asList(
            // Use the new MenuItem constructor that takes the Difficulty enum
            new MenuItem("Easy", () -> {
                GameSettings.setDifficulty(Difficulty.EASY);
                close();
            }, Difficulty.EASY), // Pass the Difficulty enum
            new MenuItem("Medium", () -> {
                GameSettings.setDifficulty(Difficulty.MEDIUM);
                close();
            }, Difficulty.MEDIUM), // Pass the Difficulty enum
            new MenuItem("Hard", () -> {
                GameSettings.setDifficulty(Difficulty.HARD);
                close();
            }, Difficulty.HARD), // Pass the Difficulty enum
            new MenuItem("Gendeng", () -> {
                GameSettings.setDifficulty(Difficulty.GENDENG);
                close();
            }, Difficulty.GENDENG), // Pass the Difficulty enum
            new MenuItem("Buroq", () -> {
                GameSettings.setDifficulty(Difficulty.BUROQ);
                close();
            }, Difficulty.BUROQ), // Pass the Difficulty enum
            new MenuItem("Back", this::close) // "Back" button has no associated difficulty
        );
    }
}
