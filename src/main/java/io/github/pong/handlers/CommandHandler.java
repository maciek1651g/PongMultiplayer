package io.github.pong.handlers;

import io.github.pong.Command;
import io.github.pong.message.Message;

public interface CommandHandler<T extends Message<?>> {

    void handle(T message);

    Class<T> handlePayload();

    Command handleCommand();
}
