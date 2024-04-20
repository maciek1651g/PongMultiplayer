public class Map {
    private final char[][] grid;
    private final int width;
    private final int height;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new char[height][width];
        clearMap();
    }

    public void clearMap() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = ' ';
            }
        }
    }

    public void setElement(int x, int y, char element) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            grid[y][x] = element;
        }
    }

    public boolean moveBall(int x, int y, char element {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            grid[y][x] = ' ';
            return true;
        }
        return false;
    }

    public char getElement(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[y][x];
        }
        return '\0';
    }

    public char[][] getGrid() {
        return grid;
    }

    public void printMap() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }
}