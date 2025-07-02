package ucupandriska.ponggame;

import ucupandriska.ponggame.object.*;
import ucupandriska.ponggame.controller.*;

public class GameState {
    public Rect playerOne, playerTwo;
    public RotatingImageObject ball;
    public PlayerController playerOneController, playerTwoController;
    public BallController ballController;
    public Text playerOneScoreText, playerTwoScoreText;
    public int playerOneScore = 0, playerTwoScore = 0;

    // Ball state for pause/unpause
    public double lastBallX, lastBallY, lastBallVX, lastBallVY;
}