package ucupandriska.ponggame;

import java.awt.*;
import ucupandriska.ponggame.util.ColorScheme;

public class GameRenderer {
    private final GameState state;

    public GameRenderer(GameState state) {
        this.state = state;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(ColorScheme.BACKGROUND);
        g2.fillRect(0, 0, 800, 600); // Use Const if you want

        state.playerOne.draw(g2);
        state.playerOneScoreText.draw(g2);
        state.playerTwo.draw(g2);
        state.playerTwoScoreText.draw(g2);

        if (state.ballController != null && state.ballController.getBallTrail() != null) {
            state.ballController.getBallTrail().draw(g2, (int) state.ball.width, (int) state.ball.height);
        }

        state.ball.draw(g2);
        state.ballController.drawCountdown(g2);
    }
}