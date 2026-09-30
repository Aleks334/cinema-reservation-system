package com.cinema.infrastructure.bus;

import com.cinema.application.port.in.Command;
import com.cinema.application.port.in.CommandBus;
import com.cinema.application.port.in.CommandHandler;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Map;

@Singleton
public class InMemoryCommandBus implements CommandBus {
    private final Map<Class<? extends Command>, CommandHandler<? extends Command>> cmdHandlers;

    @Inject
    public InMemoryCommandBus(Map<Class<? extends Command>, CommandHandler<? extends Command>> cmdHandlers) {
        this.cmdHandlers = cmdHandlers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Command> void dispatch(C command) {
        CommandHandler<C> handler = (CommandHandler<C>) cmdHandlers.get(command.getClass());
        if(handler == null) {
            throw new IllegalArgumentException("No handler registered for " + command.getClass().getName());
        }

        handler.handle(command);
    }
}
