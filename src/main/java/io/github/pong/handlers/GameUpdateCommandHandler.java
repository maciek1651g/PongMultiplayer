package io.github.pong.handlers;

import io.github.pong.Command;
import io.github.pong.GameMap;
import io.github.pong.message.GameUpdateCommand;

public class GameUpdateCommandHandler implements CommandHandler<GameUpdateCommand> {

    @Override
    public void handle(GameUpdateCommand message) {
        final GameMap gameMap = new GameMap(message.getMessageContent());
        gameMap.printMap();
    }

    @Override
    public Class<GameUpdateCommand> handlePayload() {
        return GameUpdateCommand.class;
    }

    @Override
    public Command handleCommand() {
        return Command.GAME_UPDATE;
    }
}
