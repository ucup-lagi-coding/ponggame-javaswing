package ucupandriska.ponggame.controller;

import ucupandriska.ponggame.GameWindow; // Import GameWindow
import ucupandriska.ponggame.object.Rect;
import ucupandriska.ponggame.object.RotatingImageObject;
import ucupandriska.ponggame.object.Text;
import ucupandriska.ponggame.object.BallTrail;
import ucupandriska.ponggame.util.ColorScheme;
import ucupandriska.ponggame.util.Const;
import ucupandriska.ponggame.util.Countdown;
import ucupandriska.ponggame.util.Difficulty;
import ucupandriska.ponggame.util.GameSettings;
import ucupandriska.ponggame.util.AudioLoader;

import javax.sound.sampled.Clip;
import java.awt.Color;
import java.awt.Graphics2D; // Required for drawCountdown if it uses Graphics2D

public class BallController {
    private GameWindow window; // Reference to GameWindow
    public RotatingImageObject rect;
    public Rect leftPaddle, rightPaddle;
    public Text playerOneScoreText, playerTwoScoreText;
    public Countdown timer;
    private double ballSpeed;

    private double vx = -1;
    private double vy = 0;

    private final double maxBounceAngle = Math.toRadians(60);
    private boolean ballJustScored = false;
    private boolean gameStarted = false;
    private boolean gameOver = false;

    private BallTrail ballTrail;

    private static final Clip bounceClip = AudioLoader.loadClip("/resource/audio/bounce.wav");
    private static final Clip goalClip = AudioLoader.loadClip("/resource/audio/goal.wav");

    public BallController(GameWindow window, RotatingImageObject rect, Rect leftPaddle, Rect rightPaddle,
            Text playerOneScoreText, Text playerTwoScoreText) {
        this.window = window; // Initialize GameWindow reference
        this.rect = rect;
        this.leftPaddle = leftPaddle;
        this.rightPaddle = rightPaddle;
        this.playerOneScoreText = playerOneScoreText;
        this.playerTwoScoreText = playerTwoScoreText;
        this.ballSpeed = GameSettings.getDifficulty().getBallSpeed();
        System.out.println("Ball Speed: " + ballSpeed);

        this.timer = new Countdown(3);
        this.timer.start();

        this.ballTrail = new BallTrail((int) ballSpeed / 25, ColorScheme.SCORE);

        initialMove();
    }

    public void update(double dt) {
        // If the game is over, stop updating ball and paddle physics
        if (gameOver) {
            return;
        }

        timer.update(dt);

        if (timer.isActive()) {
            // Let ball continue flying offscreen if just scored during timer countdown
            if (ballJustScored) {
                rect.x += vx * dt;
                rect.y += vy * dt;
                ballTrail.addPoint((int) rect.x, (int) rect.y);
            }
            return;
        }

        // Start ball movement after any countdown (initial or score)
        if (!gameStarted || ballJustScored) {
            centerBall();
            initialMove();
            ballJustScored = false;
            gameStarted = true;
            ballTrail.clear(); // Clear trail on new game/score reset
        }

        double rotationSpeed = ballSpeed * dt * 0.02;
        rect.rotate(rotationSpeed);

        double nextX = rect.x + vx * dt;
        double nextY = rect.y + vy * dt;

        // Left paddle collision
        if (vx < 0 &&
                rect.x <= leftPaddle.x + leftPaddle.width &&
                rect.x + rect.width >= leftPaddle.x &&
                rect.y + rect.height >= leftPaddle.y &&
                rect.y <= leftPaddle.y + leftPaddle.height) {

            double ballCenterY = rect.y + rect.height / 2;
            double paddleCenterY = leftPaddle.y + leftPaddle.height / 2;
            double distance = ballCenterY - paddleCenterY;
            double normalized = distance / (leftPaddle.height / 2);
            double bounceAngle = normalized * maxBounceAngle;

            double speed = ballSpeed + Math.abs(normalized) * 100;
            vx = Math.abs(speed * Math.cos(bounceAngle));
            vy = speed * Math.sin(bounceAngle);
            AudioLoader.playClip(bounceClip); // <-- Play bounce sound
        }

        // Right paddle collision
        if (vx > 0 &&
                rect.x + rect.width >= rightPaddle.x &&
                rect.x <= rightPaddle.x + rightPaddle.width &&
                rect.y + rect.height >= rightPaddle.y &&
                rect.y <= rightPaddle.y + rightPaddle.height) {

            double ballCenterY = rect.y + rect.height / 2;
            double paddleCenterY = rightPaddle.y + rightPaddle.height / 2;
            double distance = ballCenterY - paddleCenterY;
            double normalized = distance / (rightPaddle.height / 2);
            double bounceAngle = normalized * maxBounceAngle;

            double speed = ballSpeed + Math.abs(normalized) * 100;
            vx = -Math.abs(speed * Math.cos(bounceAngle));
            vy = speed * Math.sin(bounceAngle);
            AudioLoader.playClip(bounceClip); // <-- Play bounce sound
        }

        // Top and bottom wall bounce
        if (nextY <= Const.TOOLBAR_HEIGHT || nextY + rect.height >= Const.SCREEN_HEIGHT - Const.INSETS_BOTTOM) {
            vy *= -1;
            AudioLoader.playClip(bounceClip); // <-- Play bounce sound
        }

        // Score conditions
        if (nextX <= 0 && !ballJustScored) {
            AudioLoader.playClip(goalClip); // <-- Play goal sound
            score(playerTwoScoreText, "Player Two");
        } else if (nextX + rect.width >= Const.SCREEN_WIDTH && !ballJustScored) {
            AudioLoader.playClip(goalClip); // <-- Play goal sound
            score(playerOneScoreText, "Player One");
        }

        // Update ball position
        rect.x += vx * dt;
        rect.y += vy * dt;

        // Add current ball position to the trail AFTER its position has been updated
        ballTrail.addPoint((int) rect.x, (int) rect.y);
    }

    // Modified score method to include winner checking
    private void score(Text scoreText, String scorerName) {
        addScore(scoreText);

        // Check for win condition after score is added
        if (Integer.parseInt(playerOneScoreText.text) >= Const.SCORE_TO_WIN) {
            gameOver = true; // Set game over flag
            window.showGameOverMenu("Player One"); // Show game over menu
            centerBall(); // Stop ball movement
            ballTrail.clear(); // Clear any remaining trail
            return; // Exit to prevent further game logic
        } else if (Integer.parseInt(playerTwoScoreText.text) >= Const.SCORE_TO_WIN) {
            gameOver = true; // Set game over flag
            window.showGameOverMenu("Player Two"); // Show game over menu
            centerBall(); // Stop ball movement
            ballTrail.clear(); // Clear any remaining trail
            return; // Exit to prevent further game logic
        }

        // If no one won, proceed with the usual reset delay
        startResetDelay();
        ballTrail.clear(); // Clear trail when a score happens and game is not over
    }

    public void initialMove() {
        double angle = Math.toRadians((Math.random() - 0.5) * 60);
        double speed = ballSpeed;
        vx = Math.signum(Math.random() - 0.5) * speed * Math.cos(angle);
        vy = speed * Math.sin(angle);
    }

    public void centerBall() {
        rect.x = Const.SCREEN_WIDTH / 2 - rect.width / 2;
        rect.y = Const.SCREEN_HEIGHT / 2 - rect.height / 2;
        vx = 0;
        vy = 0;
    }

    public void startResetDelay() {
        timer.start();
        ballJustScored = true;
    }

    public void reset() {
        centerBall();
        initialMove();
        ballJustScored = false;
        gameOver = false; // Reset game over flag on full reset
        ballTrail.clear(); // Clear trail on full game reset
    }

    public void addScore(Text scoreText) {
        int score = Integer.parseInt(scoreText.text);
        score++;
        scoreText.text = String.valueOf(score);
    }

    public void drawCountdown(java.awt.Graphics2D g2) {
        timer.draw(g2);
    }

    public BallTrail getBallTrail() {
        return ballTrail;
    }

    public double getVX() {
        return vx;
    }

    public double getVY() {
        return vy;
    }

    public void setVX(double vx) {
        this.vx = vx;
    }

    public void setVY(double vy) {
        this.vy = vy;
    }
}
