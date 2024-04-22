package io.github.pong;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class Server {

    private final ServerSocket serverSocket;
    private final List<GameRoom> rooms;

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

    public GameRoom getRoomById(String roomId) {
        return rooms.stream()
                .filter(room -> Objects.equals(room.getId(), roomId))
                .findAny()
                .orElse(null);
    }
}
