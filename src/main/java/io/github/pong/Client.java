package io.github.pong;

import io.github.pong.handlers.ClientCommandHandler;

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
                if (response.contains("Room is now full")) {
                    handleGameLoop();
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
                clientCommandHandler.handleCommand(response);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleMainLoop() {
        final BufferedReader sysInput = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
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
        System.out.print("\033[H\033[2J");

        System.out.println("1. Request available rooms");
        System.out.println("2. Exit");
        System.out.print("Enter your choice: ");
    }

    private void handleMainMenuInput(final String input) throws IOException {
        switch (input) {
            case "1":
                handleJoinRoom();
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
        final BufferedReader sysInput = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        final String roomId = sysInput.readLine();
        joinRoom(roomId);
    }
}
