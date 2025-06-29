package ucupandriska.ponggame.util;

public enum Difficulty {
    EASY(250, 250, 40),
    MEDIUM(300, 250, 25),
    HARD(400, 300, 12),
    GENDENG(800, 500, 6),
    BUROQ(1200, 700, 2);

    private final double ballSpeed;
    private final double paddleSpeed;
    private final double reactionMargin;

    Difficulty(double ballSpeed, double paddleSpeed, double reactionMargin) {
        this.ballSpeed = ballSpeed;
        this.paddleSpeed = paddleSpeed;
        this.reactionMargin = reactionMargin;
    }

    public double getBallSpeed() {
        return ballSpeed;
    }

    public double getPaddleSpeed() {
        return paddleSpeed;
    }

    public double getReactionMargin() {
        return reactionMargin;
    }
}