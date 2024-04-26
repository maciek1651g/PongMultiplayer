package io.github.pong;

import lombok.Getter;

import java.util.*;

@Getter
public class GameRoom {

    private final String id;
    private final int capacity;
    private final List<ClientHandler> clients;
    private final GameMap map;
    private final Ball ball;
    private final Pallette paletteLeft;
    private final Pallette paletteRight;

    private Timer timer;

    public GameRoom(int capacity) {
        this.id = UUID.randomUUID().toString();
        this.capacity = capacity;
        this.clients = new ArrayList<>();

        map = new GameMap(100, 100);
        paletteLeft = new Pallette(45, 0, 10, 2); // Paletka górna
        paletteRight = new Pallette(45, 98, 10, 2); // Paletka dolna
        ball = new Ball(50, 50, 1, 1, paletteLeft, paletteRight); // Utworzenie piłki
    }

    public String getId() {
        return id;
    }

    public void addClient(ClientHandler clientHandler) {
        if (clients.size() < capacity) {
            clients.add(clientHandler);
            System.out.println("Client added to room " + id);
        } else {
            System.out.println("Room " + id + " is full.");
        }
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("Client removed from room " + id);
    }

    public boolean isFull() {
        return clients.size() == capacity;
    }

    public void clearRoom() {
        clients.clear();
        System.out.println("Room " + id + " cleared.");
    }

    public void startGame() {
        // Ustawienie timera do aktualizacji gry
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateGame();
            }
        }, 0, 100); // Aktualizacja co 20 ms
    }

    private void updateGame() {
        // Aktualizacja stanu gry, np. ruch piłki i aktualizacja mapy
        ball.move();
        map.clearMap();
        boolean isBallOutside = map.moveBall(ball.getX(), ball.getY(), 'O');
        map.setElement(paletteLeft.getX(), paletteLeft.getY(), '|');
        map.setElement(paletteRight.getX(), paletteRight.getY(), '|');
        broadcastMapToClients();

        if (isBallOutside) {
            // Zatrzymanie gry i wysłanie informacji o zakończeniu gry
            timer.cancel();
            for (ClientHandler client : clients) {
                client.sendGameEnd();
            }
            clearRoom();
        }
    }

    private void broadcastMapToClients() {
        clients.forEach(client -> client.sendMap(map.getGrid()));
    }

    @Override
    public String toString() {
        return "Room ID: " + id + " (" + clients.size() + "/" + capacity + " players)";
    }
}
