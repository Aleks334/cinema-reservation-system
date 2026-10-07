package com.cinema.shared;

public interface CommandBus {
     <C extends Command> void dispatch(C command);
}
