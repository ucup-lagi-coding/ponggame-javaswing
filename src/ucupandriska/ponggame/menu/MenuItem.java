package ucupandriska.ponggame.menu;

import ucupandriska.ponggame.util.Difficulty;

public class MenuItem {
    private String label;
    private Runnable action;
    private Difficulty difficulty; // Optional: Stores the associated difficulty for this item

    public MenuItem(String label, Runnable action) {
        this(label, action, null);
    }

    public MenuItem(String label, Runnable action, Difficulty difficulty) {
        this.label = label;
        this.action = action;
        this.difficulty = difficulty;
    }

    public String getLabel() {
        return label;
    }

    public void activate() {
        action.run();
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
