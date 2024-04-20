import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameRoom {
    private static int nextId = 1; // Statyczna zmienna przechowująca następne dostępne ID
    private final int id;
    private final int capacity;
    private final List<ClientHandler> clients;
    private final Map map;
    private final Ball ball;
    private Pallette palletteTop, palletteBottom;

    private Timer timer;

    public GameRoom(int capacity) {
        this.id = nextId++; // Przypisanie następnego dostępnego ID i zwiększenie licznika
        this.capacity = capacity;
        this.clients = new ArrayList<>();

        map = new Map(100, 100);
        ball = new Ball(50, 50, 1, 1, palletteTop, palletteBottom); // Utworzenie piłki
        palletteTop = new Pallette(45, 0, 10, 2); // Paletka górna
        palletteBottom = new Pallette(45, 98, 10, 2); // Paletka dolna
    }

    public int getId() {
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
        }, 0, 20); // Aktualizacja co 20 ms
    }

    public List<ClientHandler> getClients() {
        return this.clients;
    }

    private void updateGame() {
        // Aktualizacja stanu gry, np. ruch piłki i aktualizacja mapy
        ball.move();
        map.clearMap();
        boolean isBallOutside = map.moveBall(ball.getX(), ball.getY(), 'O');
        map.setElement(palletteTop.getX(), palletteTop.getY(), '|');
        map.setElement(palletteBottom.getX(), palletteBottom.getY(), '|');
        broadcastMapToClients();

        if (isBallOutside) {
            // Zatrzymanie gry i wysłanie informacji o zakończeniu gry
            timer.cancel();
            for (ClientHandler client : clients) {
                client.sendGameEnd();
            }
        }
    }

    private void broadcastMapToClients() {
        for (ClientHandler client : clients) {
            client.sendMap(map.getGrid()); // Wysyłanie mapy do klientów
        }
    }

    @Override
    public String toString() {
        return "Room ID: " + id + " (" + clients.size() + "/" + capacity + " players)";
    }
}
