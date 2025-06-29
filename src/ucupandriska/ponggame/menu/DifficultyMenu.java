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
import ucupandriska.ponggame.util.AudioLoader;

public class DifficultyMenu extends AbstractMenu {

    public DifficultyMenu(GameWindow window) {
        super(window);
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
                new MenuItem("Easy", () -> {
                    GameSettings.setDifficulty(Difficulty.EASY);
                    close();
                }, Difficulty.EASY),
                new MenuItem("Medium", () -> {
                    GameSettings.setDifficulty(Difficulty.MEDIUM);
                    close();
                }, Difficulty.MEDIUM),
                new MenuItem("Hard", () -> {
                    GameSettings.setDifficulty(Difficulty.HARD);
                    close();
                }, Difficulty.HARD),
                new MenuItem("Gendeng", () -> {
                    GameSettings.setDifficulty(Difficulty.GENDENG);
                    close();
                }, Difficulty.GENDENG),
                new MenuItem("Buroq", () -> {
                    GameSettings.setDifficulty(Difficulty.BUROQ);
                    close();
                }, Difficulty.BUROQ),
                new MenuItem("Back", this::close));
    }

    @Override
    protected void processInput() {
        boolean up = getKl().isKeyPressed(Const.BIND_UP) || getKl().isKeyPressed(Const.BIND_UP_ALT);
        boolean down = getKl().isKeyPressed(Const.BIND_DOWN) || getKl().isKeyPressed(Const.BIND_DOWN_ALT);
        boolean enter = getKl().isKeyPressed(java.awt.event.KeyEvent.VK_ENTER)
                || getKl().isKeyPressed(java.awt.event.KeyEvent.VK_SPACE);

        if (up && !isUpLast()) {
            do {
                setSelectedIndex((getSelectedIndex() - 1 + items.size()) % items.size());
            } while (items.get(getSelectedIndex()).getDifficulty() != null &&
                    items.get(getSelectedIndex()).getDifficulty() == GameSettings.getDifficulty());
            AudioLoader.playClip(getBlipselectclip());
        }
        if (down && !isDownLast()) {
            do {
                setSelectedIndex((getSelectedIndex() + 1) % items.size());
            } while (items.get(getSelectedIndex()).getDifficulty() != null &&
                    items.get(getSelectedIndex()).getDifficulty() == GameSettings.getDifficulty());
            AudioLoader.playClip(getBlipselectclip());
        }
        if (enter && !isEnterLast()) {
            AudioLoader.playClip(getClickclip());
            items.get(getSelectedIndex()).activate();
        }

        setUpLast(up);
        setDownLast(down);
        setEnterLast(enter);
    }

    // Add these protected getters/setters in AbstractMenu if not already present:
    // protected KL getKL() { return kl; }
    // protected int getSelectedIndex() { return selectedIndex; }
    // protected void setSelectedIndex(int idx) { selectedIndex = idx; }
    // protected boolean isUpLast() { return upLast; }
    // protected void setUpLast(boolean v) { upLast = v; }
    // protected boolean isDownLast() { return downLast; }
    // protected void setDownLast(boolean v) { downLast = v; }
    // protected boolean isEnterLast() { return enterLast; }
    // protected void setEnterLast(boolean v) { enterLast = v; }
    // protected Clip getBlipSelectClip() { return blipSelectClip; }
    // protected Clip getClickClip() { return clickClip; }
}
