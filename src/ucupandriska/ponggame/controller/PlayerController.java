package ucupandriska.ponggame.controller;

import ucupandriska.ponggame.object.Rect;
import ucupandriska.ponggame.object.RotatingImageObject;
import ucupandriska.ponggame.util.Const;
import ucupandriska.ponggame.util.Difficulty;
import ucupandriska.ponggame.util.GameSettings;
import ucupandriska.ponggame.util.KL;

public class PlayerController {
    public Rect rect;
    public KL keyListener;
    public int playerBindUp, playerBindDown;
    private double paddleSpeed;

    private BotController botController = null;
    private boolean isBot = false;
    private RotatingImageObject ball;

    public PlayerController(Rect rect, KL keyListener, int playerBindUp, int playerBindDown) {
        this.rect = rect;
        this.keyListener = keyListener;
        this.playerBindUp = playerBindUp;
        this.playerBindDown = playerBindDown;
        this.paddleSpeed = this.paddleSpeed = GameSettings.getDifficulty().getPaddleSpeed();
        System.out.println("Paddle Speed: " + paddleSpeed);
    }

    public PlayerController(Rect rect, RotatingImageObject ball) {
        this.rect = rect;
        this.isBot = true;
        this.ball = ball;
        this.paddleSpeed = GameSettings.getDifficulty().getPaddleSpeed();
    }

    public void enableBot(RotatingImageObject ball) {
        this.botController = new BotController(this.rect, ball);
    }

    public void update(double dt) {
        if (botController != null) {
            botController.update(dt);
        } else if (keyListener != null) {
            if (keyListener.isKeyPressed(playerBindUp)) {
                moveUp(dt);
            } else if (keyListener.isKeyPressed(playerBindDown)) {
                moveDown(dt);
            }
        }
    }

    public void moveUp(double dt) {
        if (rect.y + paddleSpeed * dt > Const.TOOLBAR_HEIGHT) {
            this.rect.y -= paddleSpeed * dt;
        }

    }

    public void moveDown(double dt) {
        if ((rect.y + paddleSpeed * dt) + rect.height < Const.SCREEN_HEIGHT - Const.INSETS_BOTTOM) {
            this.rect.y += paddleSpeed * dt;
        }
    }
}
