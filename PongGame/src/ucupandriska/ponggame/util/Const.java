package ucupandriska.ponggame.util;
import java.awt.event.KeyEvent;

public class Const {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    public static final String WINDOW_TITLE = "Pong Game";

    public static final double FPS = 60.0;
    public static final double FRAME_TIME = 1.0 / FPS; 

    public static final double PADDLE_WIDTH = 30.0;
    public static final double PADDLE_HEIGHT = 90.0;
    public static final double BALL_WIDTH = 25.0;
    public static final double H_PADDING = 20.0;
    public static final double V_PADDING = 20.0;

    public static final double MV_INCREMENT = 300;
    public static final double BALL_SPEED = 400;

    public static double TOOLBAR_HEIGHT;
    public static double INSETS_BOTTOM;

    public static final int BIND_UP = KeyEvent.VK_UP;
    public static final int BIND_DOWN = KeyEvent.VK_DOWN;
    public static final int BIND_UP_ALT = KeyEvent.VK_W;
    public static final int BIND_DOWN_ALT = KeyEvent.VK_S;

    public static final String FONT_PATH = "/resource/fonts/ThaleahFat.ttf";

    public static final double SCORE_FONT_SIZE = 96f;
    public static final double PLAYER_ONE_SCORE_POS_X = SCREEN_WIDTH * (1.0 / 8.0);
    public static final double PLAYER_TWO_SCORE_POS_X = SCREEN_WIDTH * (7.0 / 8.0) - (SCORE_FONT_SIZE / 2);
    public static final double SCORE_POS_Y = 160;

    public static final double COUNTFOWN_FONT_SIZE = 196f;

    public static final int SCORE_TO_WIN = 10;
}
