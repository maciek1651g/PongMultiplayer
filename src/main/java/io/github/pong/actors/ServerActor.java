package io.github.pong.actors;

import com.google.gson.Gson;
import io.github.pong.GameRoom;
import io.github.pong.Server;
import io.github.pong.handlers.ClientCommandHandler;
import io.github.pong.handlers.GameOverCommandHandler;
import io.github.pong.handlers.GameUpdateCommandHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ServerActor {
    public static void main(String[] args) {
        try {
            Server server = new Server(
                    new ServerSocket(12345),
                    createRooms()
            );
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<GameRoom> createRooms() {
        return IntStream.range(0, 3)
                .mapToObj(i -> new GameRoom(2))
                .collect(Collectors.toList());
    }
}
