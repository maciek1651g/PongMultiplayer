package io.github.pong;

import lombok.Getter;


@Getter
public class GameMap {

    private final Character[][] grid;
    private final int width;
    private final int height;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Character[height][width];
        clearMap();
    }

    public GameMap(Character[][] map) {
        this.grid = map;
        this.width = map[0].length;
        this.height = map.length;
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

    public boolean moveBall(int x, int y, char element) {
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

    public void printMap() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }
}