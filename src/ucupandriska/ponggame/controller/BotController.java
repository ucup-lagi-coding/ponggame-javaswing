package ucupandriska.ponggame.controller;

import ucupandriska.ponggame.object.Rect;
import ucupandriska.ponggame.object.RotatingImageObject;
import ucupandriska.ponggame.util.Const;
import ucupandriska.ponggame.util.GameSettings;

public class BotController {
    private Rect rect;
    private RotatingImageObject ball;
    private double paddleSpeed;
    private double reactionMargin;

    public BotController(Rect rect, RotatingImageObject ball) {
        this.rect = rect;
        this.ball = ball;
        this.paddleSpeed = GameSettings.getDifficulty().getPaddleSpeed();
        this.reactionMargin = GameSettings.getDifficulty().getReactionMargin();
    }

    public void update(double dt) {
        double paddleCenter = rect.y + rect.height / 2;
        double ballCenter = ball.y + ball.height / 2;
        if (Math.abs(paddleCenter - ballCenter) > reactionMargin) {
            if (paddleCenter < ballCenter) {
                moveDown(dt);
            } else {
                moveUp(dt);
            }
        }
    }

    private void moveUp(double dt) {
        if (rect.y - paddleSpeed * dt > Const.TOOLBAR_HEIGHT) {
            rect.y -= paddleSpeed * dt;
        }
    }

    private void moveDown(double dt) {
        if ((rect.y + paddleSpeed * dt) + rect.height < Const.SCREEN_HEIGHT - Const.INSETS_BOTTOM) {
            rect.y += paddleSpeed * dt;
        }
    }
}