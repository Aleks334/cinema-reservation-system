package com.cinema.application.port.in.command;

@FunctionalInterface
public interface CommandHandler<C extends Command> {
    void handle(C command);
}
