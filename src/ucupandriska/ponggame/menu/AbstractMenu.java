package ucupandriska.ponggame.menu;

import ucupandriska.ponggame.GameWindow;
import ucupandriska.ponggame.util.KL;
import ucupandriska.ponggame.object.Text;
import ucupandriska.ponggame.util.Const;
import ucupandriska.ponggame.util.FontLoader;
import ucupandriska.ponggame.util.GameSettings;
import ucupandriska.ponggame.util.Time;
import ucupandriska.ponggame.util.ColorScheme;
import ucupandriska.ponggame.util.MenuInfoHelper;
import ucupandriska.ponggame.util.AudioLoader;
import javax.sound.sampled.Clip;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.List;

public abstract class AbstractMenu {
    protected final GameWindow window;
    private final KL kl;

    private Text titleText;
    protected List<MenuItem> items;
    protected Text[] itemTexts;

    private int selectedIndex = 0;
    private boolean running = false;
    private boolean upLast = false;
    private boolean downLast = false;
    private boolean enterLast = false;
    private boolean initialized = false;

    private static final Clip clickClip = AudioLoader.loadClip("/resource/audio/click.wav");
    private static final Clip blipSelectClip = AudioLoader.loadClip("/resource/audio/blipSelect.wav");

    public AbstractMenu(GameWindow window) {
        this.window = window;
        this.kl = new KL();
    }

    protected abstract Text createTitle(Font titleFont);

    protected abstract List<MenuItem> createMenuItems();

    private void initializeMenu() {
        if (initialized)
            return;
        Font titleFont = FontLoader.loadFont(Const.FONT_PATH, 96f);
        Font itemFont = FontLoader.loadFont(Const.FONT_PATH, 48f);
        this.titleText = createTitle(titleFont);
        this.items = createMenuItems();
        this.itemTexts = new Text[items.size()];
        for (int i = 0; i < items.size(); i++) {
            double y = 250 + i * 60;
            Text t = new Text(items.get(i).getLabel(), itemFont, Const.SCREEN_WIDTH / 2.0, y);
            t.setCentered(true);
            t.setColor(ColorScheme.TEXT_PRIMARY);
            itemTexts[i] = t;
        }
        initialized = true;
    }

    public void show() {
        initializeMenu();
        running = true;
        double last = Time.getTime();
        window.removeKeyListener(window.keyListener);
        window.addKeyListener(this.kl);
        while (running) {
            double now = Time.getTime();
            if (now - last >= Const.FRAME_TIME) {
                last = now;
                processInput();
                render();
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException ignored) {
            }
        }
        window.removeKeyListener(this.kl);
        window.addKeyListener(window.keyListener);
        initialized = false;
    }

    protected void processInput() {
        boolean up = kl.isKeyPressed(Const.BIND_UP) || kl.isKeyPressed(Const.BIND_UP_ALT);
        boolean down = kl.isKeyPressed(Const.BIND_DOWN) || kl.isKeyPressed(Const.BIND_DOWN_ALT);
        boolean enter = kl.isKeyPressed(java.awt.event.KeyEvent.VK_ENTER)
                || kl.isKeyPressed(java.awt.event.KeyEvent.VK_SPACE);

        if (up && !upLast) {
            selectedIndex = (selectedIndex - 1 + items.size()) % items.size();
            AudioLoader.playClip(blipSelectClip); // Play blip when moving up
        }
        if (down && !downLast) {
            selectedIndex = (selectedIndex + 1) % items.size();
            AudioLoader.playClip(blipSelectClip); // Play blip when moving down
        }
        if (enter && !enterLast) {
            AudioLoader.playClip(clickClip); // Play click when selecting
            items.get(selectedIndex).activate();
        }

        upLast = up;
        downLast = down;
        enterLast = enter;
    }

    protected KL getKl() {
        return kl;
    }

    protected int getSelectedIndex() {
        return selectedIndex;
    }

    protected void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    protected boolean isUpLast() {
        return upLast;
    }

    protected void setUpLast(boolean upLast) {
        this.upLast = upLast;
    }

    protected boolean isDownLast() {
        return downLast;
    }

    protected void setDownLast(boolean downLast) {
        this.downLast = downLast;
    }

    protected boolean isEnterLast() {
        return enterLast;
    }

    protected void setEnterLast(boolean enterLast) {
        this.enterLast = enterLast;
    }

    protected static Clip getBlipselectclip() {
        return blipSelectClip;
    }

    protected static Clip getClickclip() {
        return clickClip;
    }

    private String getInfoForMenuItem(int index) {
        return MenuInfoHelper.getInfoForMenuItem(items.get(index).getLabel());
    }

    private void render() {
        if (titleText == null || itemTexts == null) {
            System.err.println("AbstractMenu: render called before initialization completed.");
            return;
        }
        Image buffer = window.createImage(window.getWidth(), window.getHeight());
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        drawBackground(g2);
        titleText.draw(g2);
        for (int i = 0; i < itemTexts.length; i++) {
            Color itemColor = ColorScheme.TEXT_PRIMARY;
            if (items.get(i).getDifficulty() != null &&
                    items.get(i).getDifficulty() == GameSettings.getDifficulty()) {
                itemColor = ColorScheme.TEXT_ACTIVE_DIFFICULTY;
            }
            if (i == selectedIndex) {
                itemColor = ColorScheme.TEXT_HIGHLIGHT;
            }
            itemTexts[i].setColor(itemColor);
            itemTexts[i].draw(g2);
        }
        String info = getInfoForMenuItem(selectedIndex);
        Font infoFont = FontLoader.loadFont(Const.FONT_PATH, 24f);
        Text infoText = new Text(info, infoFont, Const.SCREEN_WIDTH / 2.0, Const.SCREEN_HEIGHT - 40);
        infoText.setCentered(true);
        infoText.setColor(ColorScheme.TEXT_PRIMARY);
        infoText.draw(g2);
        window.getGraphics().drawImage(buffer, 0, 0, null);
    }

    protected void drawBackground(Graphics2D g2) {
        g2.setColor(ColorScheme.MENU_BACKGROUND);
        g2.fillRect(0, 0, window.getWidth(), window.getHeight());
    }

    protected void close() {
        running = false;
    }
}
