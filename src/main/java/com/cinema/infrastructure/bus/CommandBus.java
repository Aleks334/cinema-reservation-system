package com.cinema.infrastructure.bus;

import com.cinema.application.ports.in.Command;

public interface CommandBus {
     <C extends Command> void dispatch(C command);
}
