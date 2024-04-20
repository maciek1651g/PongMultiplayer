import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final ServerSocket serverSocket;
    private List<GameRoom> rooms;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        initializeRooms();
    }

    public static void main(String[] args) {
        try {
            Server server = new Server(12345);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeRooms() {
        rooms = new ArrayList<>();

        // Tworzenie trzech pokoi dwuosobowych
        for (int i = 0; i < 3; i++) {
            rooms.add(new GameRoom(2));
        }
    }

    public void start() {
        System.out.println("Server started.");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket, this)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<GameRoom> getRooms() {
        return rooms;
    }

    public GameRoom getRoomById(String roomId) {
        for (GameRoom room : rooms) {
            if (Integer.toString(room.getId()).equals(roomId)) {
                return room;
            }
        }
        return null;
    }
}
