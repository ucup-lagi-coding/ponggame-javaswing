package ucupandriska.ponggame.menu;

public class MenuItem {
    private final String label;
    private final Runnable action;

    public MenuItem(String label, Runnable action) {
        this.label = label;
        this.action = action;
    }

    public String getLabel() {
        return label;
    }

    public void activate() {
        if (action != null) action.run();
    }

    public Object getDifficulty() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDifficulty'");
    }
}
