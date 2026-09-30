package com.cinema.application.port.in;

public interface CommandBus {
     <C extends Command> void dispatch(C command);
}
