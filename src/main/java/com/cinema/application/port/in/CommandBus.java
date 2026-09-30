package com.cinema.application.port.in;

public interface CommandBus {
     <C extends Command> void register(Class<C> commandClass, CommandHandler<C> handler);
     <C extends Command> void dispatch(C command);
}
