package ucupandriska.ponggame;

import javax.swing.JFrame;

import ucupandriska.ponggame.controller.*;
import ucupandriska.ponggame.menu.AbstractMenu;
import ucupandriska.ponggame.menu.GameOverMenu;
import ucupandriska.ponggame.menu.MainMenu;
import ucupandriska.ponggame.menu.PauseMenu;
import ucupandriska.ponggame.object.*;
import ucupandriska.ponggame.util.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent; // Make sure KeyEvent is imported

public class GameWindow extends JFrame implements Runnable {
    public Graphics2D g2;
    public KL keyListener = new KL(); // Your KL class instance
    private boolean isPaused = false;
    private PauseMenu pauseMenu;

    public Rect playerOne, playerTwo;
    public RotatingImageObject ball;
    public PlayerController playerOneController, playerTwoController;
    public BallController ballController;
    public Text playerOneScoreText, playerTwoScoreText;
    public int playerOneScore, playerTwoScore;

    Font customFontScore = FontLoader.loadFont(Const.FONT_PATH, (float)Const.SCORE_FONT_SIZE);
    
    public GameWindow(){
        this.setSize(Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
        this.setTitle(Const.WINDOW_TITLE);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        this.addKeyListener(keyListener); // Add your KL instance as the KeyListener
        
        Const.TOOLBAR_HEIGHT = this.getInsets().top;
        Const.INSETS_BOTTOM = this.getInsets().bottom;
        
        g2 = (Graphics2D)this.getGraphics();      
    }

    public void togglePause() {
        System.out.println("togglePause() called. isPaused was: " + isPaused);
        isPaused = !isPaused;
        System.out.println("togglePause() called. isPaused is now: " + isPaused);

        if (isPaused) {
            System.out.println("Game is now PAUSED. Showing pause menu.");
            pauseMenu = new PauseMenu(this);
            pauseMenu.show(); // This call blocks until the menu is closed
            System.out.println("Pause menu closed. Game should resume.");
        } else {
            System.out.println("Game is now UNPAUSED.");
        }
    }
    
    public void startGame() {
        playerOneScoreText = new Text(playerOneScore, customFontScore, Const.PLAYER_ONE_SCORE_POS_X, Const.SCORE_POS_Y);
        playerTwoScoreText = new Text(playerTwoScore, customFontScore, Const.PLAYER_TWO_SCORE_POS_X, Const.SCORE_POS_Y);

        playerOne = new Rect(Const.H_PADDING, Const.V_PADDING, Const.PADDLE_WIDTH, Const.PADDLE_HEIGHT, ColorScheme.PADDLE);
        playerOneController = new PlayerController(playerOne, keyListener, Const.BIND_UP_ALT, Const.BIND_DOWN_ALT);

        playerTwo = new Rect(Const.SCREEN_WIDTH - Const.PADDLE_WIDTH - Const.H_PADDING, Const.V_PADDING, Const.PADDLE_WIDTH, Const.PADDLE_HEIGHT, ColorScheme.PADDLE);
        playerTwoController = new PlayerController(playerTwo, keyListener, Const.BIND_UP, Const.BIND_DOWN);

        Image ballImg = ImageLoader.loadAndScaleImage("/resource/images/ball.png", (int)Const.BALL_WIDTH, (int)Const.BALL_WIDTH);
        if (ballImg == null) {
            System.err.println("Failed to load ball image!");
        }

        ball = new RotatingImageObject(Const.SCREEN_WIDTH / 2, Const.SCREEN_HEIGHT / 2, Const.BALL_WIDTH, Const.BALL_WIDTH, ballImg);

        ballController = new BallController(this, ball, playerOne, playerTwo, playerOneScoreText, playerTwoScoreText);
    }

    public void returnToMainMenu() {
        System.out.println("returnToMainMenu() called. Resetting game.");
        isPaused = false;
        resetGame();
        new MainMenu(this).show();
    }
    
    private void resetGame() {
        System.out.println("resetGame() called. Reinitializing game objects.");
        playerOneScore = 0;
        playerTwoScore = 0;
        startGame(); // This is the actual reset
    }

    public void showGameOverMenu(String winnerName) {
        System.out.println("Game Over! " + winnerName + " wins!");
        isPaused = true; // Pause the game state to prevent further updates
        GameOverMenu gameOverMenu = new GameOverMenu(this, winnerName);
        gameOverMenu.show(); // This will block until the user selects an option
        // After menu closes, the control returns here. Based on menu selections,
        // either returnToMainMenu() or System.exit(0) would have been called.
    }


    public void update(double dt) {
        // Only update game logic if not paused
        if (!isPaused) {
            // Drawing to a buffer image first to prevent flickering
            Image dbImage = createImage(getWidth(), getHeight());
            Graphics dbg = dbImage.getGraphics();
            this.draw(dbg); // Draw game elements to the buffer
            g2.drawImage(dbImage, 0, 0, this); // Draw the buffer to the screen
            
            playerOneController.update(dt);
            playerTwoController.update(dt);
            ballController.update(dt);
        }
    }
    
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(ColorScheme.BACKGROUND);
        g2.fillRect(0, 0, Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);

        playerOne.draw(g2);
        playerOneScoreText.draw(g2);
        playerTwo.draw(g2);
        playerTwoScoreText.draw(g2);
        
        if (ballController != null && ballController.getBallTrail() != null) {
            ballController.getBallTrail().draw(g2, (int)ball.width, (int)ball.height);
        }

        ball.draw(g2);
        ballController.drawCountdown(g2);
    }

    @Override
    public void run() {
        double lastFrameTime = Time.getTime();
        boolean escapeWasPressedLastFrame = false; // Debounce flag for Escape key

        while (true) {
            double currentTime = Time.getTime();
            double deltaTime = currentTime - lastFrameTime;

            // Check for Escape key press to toggle pause
            boolean currentEscapePress = keyListener.isKeyPressed(KeyEvent.VK_ESCAPE) || keyListener.isKeyPressed(KeyEvent.VK_P);
            if (currentEscapePress && !escapeWasPressedLastFrame) {
                togglePause();
            }
            escapeWasPressedLastFrame = currentEscapePress; // Update debounce flag

            if (!isPaused) {
                if (deltaTime >= Const.FRAME_TIME) {
                    lastFrameTime = currentTime;
                    update(deltaTime); // Update and draw the game
                } else {
                    try {
                        double sleepTime = Const.FRAME_TIME - deltaTime;
                        Thread.sleep(Math.max(0, (long)(sleepTime * 1000))); // Ensure sleep time isn't negative
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // If paused, the game loop doesn't update game logic.
                // The PauseMenu.show() method handles its own rendering loop and
                // will keep the main thread busy until it's closed.
                // A small sleep here prevents 100% CPU usage if somehow
                // the menu wasn't blocking the thread properly (e.g., if
                // togglePause was called without pauseMenu.show() or it returned quickly).
                try {
                    Thread.sleep(10); 
                } catch (InterruptedException ignored) {}
            }
        }
    }
}