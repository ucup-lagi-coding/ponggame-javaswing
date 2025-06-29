package ucupandriska.ponggame.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KL implements KeyListener {
    private boolean keyPressed[] = new boolean[128];

    @Override
    public void keyTyped(KeyEvent e) {
        // Do nothing: ignore keyTyped events
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int code = keyEvent.getKeyCode();
        if (code >= 0 && code < keyPressed.length) {
            keyPressed[code] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int code = keyEvent.getKeyCode();
        if (code >= 0 && code < keyPressed.length) {
            keyPressed[code] = false;
        }
    }
    
    /**
     * @param keyCode the KeyEvent.VK_* code
     * @return true if that key is currently pressed
     */
    public boolean isKeyPressed(int keyCode) {
        if (keyCode >= 0 && keyCode < keyPressed.length) {
            return keyPressed[keyCode];
        }
        return false;
    }
}
