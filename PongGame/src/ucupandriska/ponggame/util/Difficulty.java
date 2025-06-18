package ucupandriska.ponggame.util;

public enum Difficulty {
    EASY(250, 250),
    MEDIUM(300, 250),
    HARD(400, 300),
    GENDENG(800, 500),
    BUROQ(1200, 700);

    private final double ballSpeed;
    private final double paddleSpeed;

    Difficulty(double ballSpeed, double paddleSpeed) {
        this.ballSpeed = ballSpeed;
        this.paddleSpeed = paddleSpeed;
    }

    public double getBallSpeed() {
        return ballSpeed;
    }

    public double getPaddleSpeed() {
        return paddleSpeed;
    }
}