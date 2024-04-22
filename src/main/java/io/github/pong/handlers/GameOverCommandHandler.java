package io.github.pong.handlers;

import io.github.pong.Command;
import io.github.pong.message.GameOverCommand;

public class GameOverCommandHandler implements CommandHandler<GameOverCommand> {

    @Override
    public void handle(GameOverCommand message) {}

    @Override
    public Class<GameOverCommand> handlePayload() {
        return GameOverCommand.class;
    }

    @Override
    public Command handleCommand() {
        return Command.GAME_OVER;
    }
}
