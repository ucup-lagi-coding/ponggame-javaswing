package ucupandriska.ponggame.menu;

import ucupandriska.ponggame.GameWindow;
import ucupandriska.ponggame.util.KL;
import ucupandriska.ponggame.object.Text;
import ucupandriska.ponggame.util.Const;
import ucupandriska.ponggame.util.FontLoader;
import ucupandriska.ponggame.util.GameSettings; // Imported for active difficulty check
import ucupandriska.ponggame.util.Time;
import ucupandriska.ponggame.util.ColorScheme;
import ucupandriska.ponggame.util.Difficulty; // Imported for MenuItem and active difficulty check

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.List;

public abstract class AbstractMenu {
    protected final GameWindow window;
    private final KL kl;

    // These fields are now NOT final and will be initialized later
    private Text titleText;
    protected List<MenuItem> items; // Changed to protected for subclass access
    protected Text[] itemTexts;     // Changed to protected for subclass access

    private int selectedIndex = 0;
    private boolean running = false;
    private boolean upLast = false;
    private boolean downLast = false;
    private boolean enterLast = false;
    private boolean initialized = false; // New flag to ensure initialization happens only once per menu instance

    // Moved MenuItem definition here to be an inner class, as per previous suggestions
    // This allows associating Difficulty with menu items
    protected static class MenuItem {
        private String label;
        private Runnable action;
        private Difficulty difficulty; // Optional: Stores the associated difficulty for this item

        // Constructor for general menu items (e.g., "Back", "Play")
        public MenuItem(String label, Runnable action) {
            this(label, action, null); // Call the more specific constructor with null difficulty
        }

        // Constructor for difficulty-specific menu items
        public MenuItem(String label, Runnable action, Difficulty difficulty) {
            this.label = label;
            this.action = action;
            this.difficulty = difficulty;
        }

        public String getLabel() { return label; }
        public void activate() { action.run(); }
        public Difficulty getDifficulty() { return difficulty; } // Getter for the difficulty
    }

    public AbstractMenu(GameWindow window) {
        this.window = window;
        this.kl = new KL();
        // KeyListener is now added/removed in show()/close() methods, not here.
        // window.addKeyListener(kl); // Removed from here
    }

    /** Subclass provides the title Text. */
    protected abstract Text createTitle(Font titleFont);

    /** Subclass provides the menu items (labels + actions).
     * Subclasses must use AbstractMenu.MenuItem to create items. */
    protected abstract List<MenuItem> createMenuItems();

    /**
     * Initializes the menu's title and menu item Text objects.
     * This method is called by show() to ensure subclass-specific data is set first.
     */
    private void initializeMenu() {
        if (initialized) {
            return; // Already initialized for this instance
        }

        // Load fonts once
        Font titleFont = FontLoader.loadFont(Const.FONT_PATH, 96f);
        Font itemFont  = FontLoader.loadFont(Const.FONT_PATH, 48f);

        // Build title and items - now called *after* subclass constructor has had a chance to run
        this.titleText = createTitle(titleFont);
        this.items     = createMenuItems();
        this.itemTexts = new Text[items.size()];

        // Position each menu Text
        for (int i = 0; i < items.size(); i++) {
            double y = 250 + i * 60;
            Text t = new Text(items.get(i).getLabel(), itemFont, Const.SCREEN_WIDTH/2.0, y);
            t.setCentered(true);
            t.setColor(ColorScheme.TEXT_PRIMARY); // Default color
            itemTexts[i] = t; // Assigning the actual Text object
        }
        initialized = true; // Mark as initialized
    }


    /** Call to enter this menu; blocks until an item’s action closes it. */
    public void show() {
        // CRITICAL FIX: Initialize menu components here
        // This ensures subclass specific fields (like winnerName in GameOverMenu)
        // are set before createTitle is called.
        initializeMenu();

        running = true;
        double last = Time.getTime();

        // Add this menu's key listener and remove the game's listener
        window.removeKeyListener(window.keyListener); // Remove GameWindow's default listener
        window.addKeyListener(this.kl); // Add this menu's specific listener

        while (running) {
            double now = Time.getTime();
            if (now - last >= Const.FRAME_TIME) {
                last = now;
                processInput();
                render();
            }
            try { Thread.sleep(2); } catch (InterruptedException ignored) {}
        }
        // When menu closes, re-add the game's key listener
        window.removeKeyListener(this.kl); // Remove this menu's listener
        window.addKeyListener(window.keyListener); // Re-add GameWindow's default listener
        
        // Reset initialized flag if the menu might be reopened with different dynamic content
        // For GameOverMenu, it's usually new instance per game, but for MainMenu/PauseMenu, it's reused.
        // For dynamic titles like "Player X Wins!", it's safer to reset.
        initialized = false;
    }

    private void processInput() {
        boolean up    = kl.isKeyPressed(Const.BIND_UP)    || kl.isKeyPressed(Const.BIND_UP_ALT);
        boolean down  = kl.isKeyPressed(Const.BIND_DOWN)  || kl.isKeyPressed(Const.BIND_DOWN_ALT);
        boolean enter = kl.isKeyPressed(KeyEvent.VK_ENTER) || kl.isKeyPressed(KeyEvent.VK_SPACE);

        if (up && !upLast) {
            selectedIndex = (selectedIndex - 1 + items.size()) % items.size();
        }

        if (down && !downLast) {
            selectedIndex = (selectedIndex + 1) % items.size();
        }

        if (enter && !enterLast) {
            items.get(selectedIndex).activate();
        }

        // update debounce flags
        upLast = up;
        downLast = down;
        enterLast = enter;
    }


    private void render() {
        // Add a null check for titleText and itemTexts. This should prevent issues
        // if render() is somehow called before initializeMenu() completes,
        // though initializeMenu() is now called at the start of show().
        if (titleText == null || itemTexts == null) {
            // This situation should ideally not happen with the initializeMenu() call in show().
            // However, it's a defensive check.
            System.err.println("AbstractMenu: render called before initialization completed.");
            return;
        }

        Image buffer = window.createImage(window.getWidth(), window.getHeight());
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();

        // background
        drawBackground(g2);

        // draw title and items
        titleText.draw(g2);
        for (int i = 0; i < itemTexts.length; i++) {
            Color itemColor = ColorScheme.TEXT_PRIMARY;

            // This part requires MenuItem to have a getDifficulty() method and
            // for items to be created with the Difficulty enum.
            // Ensure DifficultyMenu correctly uses the MenuItem(label, action, difficulty) constructor.
            if (items.get(i).getDifficulty() != null &&
                items.get(i).getDifficulty() == GameSettings.getDifficulty()) {
                
                itemColor = ColorScheme.TEXT_ACTIVE_DIFFICULTY;
            }

            // Check if this item is currently selected in the menu
            if (i == selectedIndex) {
                itemColor = ColorScheme.TEXT_HIGHLIGHT;
            }
            
            itemTexts[i].setColor(itemColor);
            itemTexts[i].draw(g2);
        }

        window.getGraphics().drawImage(buffer, 0, 0, null);
    }

    protected void drawBackground(Graphics2D g2) {
        g2.setColor(ColorScheme.MENU_BACKGROUND);
        g2.fillRect(0, 0, window.getWidth(), window.getHeight());
    }

    /** Subclasses should call this to close the menu loop. */
    protected void close() {
        running = false;
    }
}
