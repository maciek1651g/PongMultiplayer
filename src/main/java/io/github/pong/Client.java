package io.github.pong;

import com.google.gson.Gson;
import io.github.pong.handlers.ClientCommandHandler;
import io.github.pong.message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {

    final BufferedReader sysInput = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
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

    public void receiveRoomsResponse() {
        try {
            String response;
            while (null != (response = input.readLine())) {

                if ("end_of_response".equals(response)) {
                    break;  // Przerwanie pętli, gdy serwer zasygnalizuje koniec transmisji
                }
                System.out.println(response);
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
                if (response.contains("Room is now full. Game will start soon.")) {
                    // start game
                    handleGameLoop();
                    break;
                }
                if (response.contains("Room is full")) {
                    // room is full
                    break;
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
                //  clientCommandHandler.handleCommand(response);
                var parsed = new Gson().fromJson(response, HashMap.class);
                var gameMap = parsed.get("messageContent");

                if (!(gameMap instanceof List)) {
                    // Game Over
                    return;
                }

                var content = (List<List>) gameMap;

                for (List list : content) {

                    for (Object object : list) {
                        System.out.print(object);
                    }

                    System.out.println();
                }

                //  print map

                System.out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleMainLoop() {
        while (true) {
            try {
                while (true) {
                    printMainMenu();

                    final String input = sysInput.readLine();
                    handleMainMenuInput(input);
                }
            } catch (final IOException e) {
                e.printStackTrace();
                System.out.println("Connection error. Please try again later.");
            }
        }
    }

    private void printMainMenu() {
        //clear console
        System.out.flush();

        System.out.println("1. Request available rooms");
        System.out.println("2. Exit");
        System.out.print("Enter your choice: ");
    }

    private void handleMainMenuInput(final String input) throws IOException {
        switch (input) {
            case "1":
                handleJoinRoom();
                System.out.print("Press Enter to continue...");
                sysInput.readLine();
                break;
            case "2":
                close();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input. Please try again.");
        }
    }

    private void handleJoinRoom() throws IOException {
        sendRequestForRooms();
        receiveRoomsResponse();
        System.out.print("Enter the ID of the room you want to join: ");
        final String roomId = sysInput.readLine();
        joinRoom(roomId);
    }
}
