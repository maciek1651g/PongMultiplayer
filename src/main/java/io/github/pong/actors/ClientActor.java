package io.github.pong.actors;

import com.google.gson.Gson;
import io.github.pong.Client;
import io.github.pong.handlers.ClientCommandHandler;
import io.github.pong.handlers.GameOverCommandHandler;
import io.github.pong.handlers.GameUpdateCommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ClientActor {
    public static void main(String[] args) {
        try {
            System.out.println("Press Enter to connect to the server...");
            final BufferedReader sysInput = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            sysInput.readLine(); // Czekanie na wciśnięcie Enter

            final Client client = new Client(
                    "localhost", 12345,
                    new ClientCommandHandler(
                            List.of(
                                    new GameUpdateCommandHandler(),
                                    new GameOverCommandHandler()
                            ), new Gson())
            );
            client.handleMainLoop();
        } catch (final IOException e) {
            e.printStackTrace();
            System.out.println("Connection error. Please try again later.");
        }
    }
}
