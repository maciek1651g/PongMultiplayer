package io.github.pong.message;

import io.github.pong.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Message<T> {
    private Command command;
    private T messageContent;
}