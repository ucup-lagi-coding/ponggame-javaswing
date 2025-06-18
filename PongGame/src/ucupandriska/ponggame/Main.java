package ucupandriska.ponggame;

import javax.swing.SwingUtilities;
import ucupandriska.ponggame.menu.MainMenu;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow();

            // Run menu logic in a background thread so UI stays responsive
            Thread menuThread = new Thread(() -> {
                new MainMenu(window).show();
                new Thread(window).start();  // Start the game after menu finishes
            });
            menuThread.start();
        });
    }
}
