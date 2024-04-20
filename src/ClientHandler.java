import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Server server;
    private final BufferedReader input;
    private final PrintWriter output;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            String request;
            while ((request = input.readLine()) != null) {
                if (request.equals("request_rooms_modes")) {
                    handleRoomsAndModesRequest();
                } else if (request.startsWith("join_room")) {
                    handleJoinRoomRequest(request);
                } else {
                    System.out.println("Unknown request from client: " + request);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRoomsAndModesRequest() {
        StringBuilder response = new StringBuilder();
        response.append("Available Rooms and Modes:\n");
        response.append("2-Player Rooms:\n");
        for (GameRoom room : server.getRooms()) {
            response.append(room.toString()).append("\n");
        }
        response.append("end_of_response");

        output.println(response);
    }

    private void handleJoinRoomRequest(String request) {
        // Wyodrębnienie id pokoju z zapytania
        String roomId = request.split(" ")[1];
        handleJoinRoom(roomId);
    }

    public PrintWriter getOutput() {
        return output;
    }

    public void handleJoinRoom(String roomId) {
        GameRoom room = server.getRoomById(roomId);
        if (room == null) {
            output.println("Invalid room ID");
            return;
        }

        synchronized (room) {
            if (room.isFull()) {
                output.println("Room is full");
            } else {
                room.addClient(this);
                output.println("You have joined the room. Waiting for all players to join...");

                // Sprawdzenie czy pokój jest już pełny
                if (room.isFull()) {
                    for (ClientHandler client : room.getClients()) {
                        client.getOutput().println("Room is now full. Game will start soon.");
                    }
                }
            }
        }
    }

    public void handleGameLoop() {
        // Implementacja pętli gry
        try {
            String response;
            while (null != (response = input.readLine())) {
                if (response.contains("Game over")) {
                    break;
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMap(char[][] map) {
        Message message = new Message(Command.GAME_UPDATE, map);

        // use JSON to serialize the message
        String json = new Gson().toJson(message);
        output.println(json);
    }
}
