package io.github.pong.message;

import io.github.pong.Command;

public class GameOverCommand extends Message<Void> {
    public GameOverCommand() {
        super(Command.GAME_OVER, null);
    }
}
