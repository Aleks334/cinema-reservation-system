package com.cinema.application.ports.in;

@FunctionalInterface
public interface CommandHandler<C extends Command> {
    void handle(C command);
}
