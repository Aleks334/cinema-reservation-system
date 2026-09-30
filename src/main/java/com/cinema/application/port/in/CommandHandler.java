package com.cinema.application.port.in;

@FunctionalInterface
public interface CommandHandler<C extends Command> {
    void handle(C command);
}
