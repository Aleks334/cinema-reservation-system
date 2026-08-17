package com.cinema.infrastructure.bus;

import com.cinema.application.port.in.command.Command;
import com.cinema.application.port.in.command.CommandBus;
import com.cinema.application.port.in.command.CommandHandler;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCommandBus implements CommandBus {

    private final Map<Class<? extends Command>, CommandHandler<? extends Command>> registry = new HashMap<>();

    @Override
    public <C extends Command> void register(Class<C> commandClass, CommandHandler<C> handler) {
        registry.put(commandClass, handler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Command> void dispatch(C command) {
        CommandHandler<C> handler = (CommandHandler<C>) registry.get(command.getClass());
        if(handler == null) {
            throw new IllegalArgumentException("No handler registered for " + command.getClass().getName());
        }

        handler.handle(command);
    }
}
