package com.cinema.shared;

@FunctionalInterface
public interface CommandHandler<C extends Command> {
    void handle(C command);
}
