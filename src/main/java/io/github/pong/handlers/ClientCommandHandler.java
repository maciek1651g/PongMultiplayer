package io.github.pong.handlers;

import com.google.gson.Gson;
import io.github.pong.message.Message;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class ClientCommandHandler {

    private final List<CommandHandler> commandHandlers;
    private final Gson gson;

    public void handleCommand(String payload) {
        try {
            final Message<?> message = createMessage(payload);
            commandHandlers.stream()
                    .filter(handler -> Objects.equals(handler.handleCommand(), message.getCommand()))
                    .forEach(handler -> handler.handle(createMessagePayload(payload, handler.handlePayload())));
        } catch (Exception ignored) {}
    }

    private Message<?> createMessage(String payload) {
        return gson.fromJson(payload, Message.class);
    }

    private <T extends  Message> T createMessagePayload(String payload, Class<T> clazz) {
        return gson.fromJson(payload, clazz);
    }
}
