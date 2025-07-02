package ucupandriska.ponggame;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.KeyEvent;
import javax.sound.sampled.Clip;

import ucupandriska.ponggame.controller.BallController;
import ucupandriska.ponggame.controller.PlayerController;
import ucupandriska.ponggame.menu.*;
import ucupandriska.ponggame.object.*;
import ucupandriska.ponggame.util.*;
import ucupandriska.ponggame.util.GameSettings.GameMode;

public class GameWindow extends JFrame implements Runnable {
    public Graphics2D g2;
    public KL keyListener = new KL();
    private boolean isPaused = false;
    private PauseMenu pauseMenu;

    private final GameState state = new GameState();
    private final GameRenderer renderer = new GameRenderer(state);

    Font customFontScore = FontLoader.loadFont(Const.FONT_PATH, (float) Const.SCORE_FONT_SIZE);
    private static final Clip clickClip = AudioLoader.loadClip("/resource/audio/click.wav");

    private final MenuManager menuManager = new MenuManager(this);

    public GameWindow() {
        this.setSize(Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
        this.setTitle(Const.WINDOW_TITLE);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.addKeyListener(keyListener);

        Const.TOOLBAR_HEIGHT = this.getInsets().top;
        Const.INSETS_BOTTOM = this.getInsets().bottom;

        g2 = (Graphics2D) this.getGraphics();
    }

    public void togglePause() {
        if (!isPaused) {
            if (state.ballController != null && state.ball != null) {
                state.lastBallX = state.ball.x;
                state.lastBallY = state.ball.y;
                state.lastBallVX = state.ballController.getVX();
                state.lastBallVY = state.ballController.getVY();
            }
        }

        isPaused = !isPaused;
        AudioLoader.playClip(clickClip);

        if (isPaused) {
            menuManager.showPauseMenu();
        } else {
            if (state.ballController != null && state.ball != null) {
                state.ball.x = state.lastBallX;
                state.ball.y = state.lastBallY;
                state.ballController.setVX(state.lastBallVX);
                state.ballController.setVY(state.lastBallVY);
            }
        }
    }

    public void startGame() {
        state.playerOneScoreText = new Text(state.playerOneScore, customFontScore, Const.PLAYER_ONE_SCORE_POS_X,
                Const.SCORE_POS_Y);
        state.playerTwoScoreText = new Text(state.playerTwoScore, customFontScore, Const.PLAYER_TWO_SCORE_POS_X,
                Const.SCORE_POS_Y);

        state.playerOne = new Rect(Const.H_PADDING, Const.V_PADDING, Const.PADDLE_WIDTH, Const.PADDLE_HEIGHT,
                ColorScheme.PADDLE);
        state.playerOneController = new PlayerController(state.playerOne, keyListener, Const.BIND_UP_ALT,
                Const.BIND_DOWN_ALT);

        state.playerTwo = new Rect(Const.SCREEN_WIDTH - Const.PADDLE_WIDTH - Const.H_PADDING, Const.V_PADDING,
                Const.PADDLE_WIDTH, Const.PADDLE_HEIGHT, ColorScheme.PADDLE);

        Image ballImg = ImageLoader.loadAndScaleImage("/resource/images/ball.png", (int) Const.BALL_WIDTH,
                (int) Const.BALL_WIDTH);
        state.ball = new RotatingImageObject(Const.SCREEN_WIDTH / 2, Const.SCREEN_HEIGHT / 2, Const.BALL_WIDTH,
                Const.BALL_WIDTH, ballImg);

        if (GameSettings.getGameMode() == GameMode.BOT) {
            state.playerTwoController = new PlayerController(state.playerTwo, keyListener, Const.BIND_UP,
                    Const.BIND_DOWN);
            state.playerTwoController.enableBot(state.ball);
        } else {
            state.playerTwoController = new PlayerController(state.playerTwo, keyListener, Const.BIND_UP,
                    Const.BIND_DOWN);
        }

        state.ballController = new BallController(this, state.ball, state.playerOne, state.playerTwo,
                state.playerOneScoreText, state.playerTwoScoreText);
    }

    public void returnToMainMenu() {
        isPaused = false;
        resetGame();
        menuManager.showMainMenu();
    }

    private void resetGame() {
        state.playerOneScore = 0;
        state.playerTwoScore = 0;
        startGame();
    }

    public void showGameOverMenu(String winnerName) {
        isPaused = true;
        menuManager.showGameOverMenu(winnerName);
    }

    public void update(double dt) {
        if (!isPaused) {
            Image dbImage = createImage(getWidth(), getHeight());
            Graphics dbg = dbImage.getGraphics();
            renderer.draw(dbg);
            g2.drawImage(dbImage, 0, 0, this);

            state.playerOneController.update(dt);
            state.playerTwoController.update(dt);
            state.ballController.update(dt);
        }
    }

    @Override
    public void run() {
        double lastFrameTime = Time.getTime();
        boolean escapeWasPressedLastFrame = false;

        while (true) {
            double currentTime = Time.getTime();
            double deltaTime = currentTime - lastFrameTime;

            boolean currentEscapePress = keyListener.isKeyPressed(KeyEvent.VK_ESCAPE)
                    || keyListener.isKeyPressed(KeyEvent.VK_P);
            if (currentEscapePress && !escapeWasPressedLastFrame) {
                togglePause();
            }
            escapeWasPressedLastFrame = currentEscapePress;

            if (!isPaused) {
                if (deltaTime >= Const.FRAME_TIME) {
                    lastFrameTime = currentTime;
                    update(deltaTime);
                } else {
                    try {
                        double sleepTime = Const.FRAME_TIME - deltaTime;
                        Thread.sleep(Math.max(0, (long) (sleepTime * 1000)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}