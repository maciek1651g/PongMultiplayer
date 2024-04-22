package io.github.pong;

import com.google.gson.Gson;
import io.github.pong.handlers.ClientCommandHandler;
import io.github.pong.message.GameUpdateCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {

    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;
    private final ClientCommandHandler clientCommandHandler;

    public Client(final String serverAddress, final int serverPort, ClientCommandHandler clientCommandHandler) throws IOException {
        this.socket = new Socket(serverAddress, serverPort);
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8));
        this.output = new PrintWriter(this.socket.getOutputStream(), true, StandardCharsets.UTF_8);
        this.clientCommandHandler = clientCommandHandler;
    }

    public void sendRequestForRooms() {
        this.output.println("request_rooms_modes");
    }

    public void receiveResponse() {
        try {
            String response;
            while (null != (response = input.readLine())) {

                if ("end_of_response".equals(response)) {
                    break;  // Przerwanie pętli, gdy serwer zasygnalizuje koniec transmisji
                }
                System.out.println("Server response: " + response);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void joinRoom(final String roomId) {
        this.output.println("join_room: " + roomId);
        this.receiveRoomResponse();
    }

    public void receiveRoomResponse() {
        try {
            String response;
            while (null != (response = input.readLine())) {
                System.out.println("Server response: " + response);
                if (response.contains("You have joined the room")) {
                    handleGameLoop();  // Rozpoczęcie pętli gry
                    break;
                }
                if (response.contains("Game will start soon.") || response.contains("Room is full") || response.contains("Invalid room ID")) {
                    break;  // Przerywamy oczekiwanie, gdy pokój jest pełny lub ID jest nieprawidłowe
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        this.socket.close();
    }

    public void handleGameLoop() {
        // Implementacja pętli gry
        try {
            String response;
            while (null != (response = input.readLine())) {
                System.out.println(response);
                clientCommandHandler.handleCommand(response);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
