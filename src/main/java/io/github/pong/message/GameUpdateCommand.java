package io.github.pong.message;

import io.github.pong.Command;

public class GameUpdateCommand extends Message<Character[][]> {
    public GameUpdateCommand(Character[][] payload) {
        super(Command.GAME_UPDATE, payload);
    }
}
