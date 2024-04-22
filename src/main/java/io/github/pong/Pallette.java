package io.github.pong;

public class Pallette {
    private final int x;
    private final int y; // Pozycja paletki
    private final int width;
    private final int height; // Wymiary paletki

    public Pallette(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean collidesWithBall(int ballX, int ballY) {
        // Sprawdzenie kolizji z piłką
        return ballX >= x && ballX <= x + width && ballY == y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}