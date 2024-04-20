public class Ball {
    private final int dx;
    private final Pallette palletteLeft;
    private final Pallette palletteRight; // Referencje do palettek graczy
    private int x, y; // Pozycja piłki
    private int dy; // Wektor prędkości piłki

    public Ball(int x, int y, int dx, int dy, Pallette palletteLeft, Pallette palletteRight) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.palletteLeft = palletteLeft;
        this.palletteRight = palletteRight;
    }

    public void move() {
        // Aktualizacja pozycji piłki na podstawie wektora prędkości
        x += dx;
        y += dy;

        // Kolizja z paletkami
        if (palletteLeft.collidesWithBall(x, y) || palletteRight.collidesWithBall(x, y)) {
            dy = -dy; // Zmiana kierunku po odbiciu od paletki
        }

        // Kolizja z górną i dolną ścianą
        if (y <= 0 || y >= 99) {
            dy = -dy; // Zmiana kierunku po odbiciu od ściany
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}