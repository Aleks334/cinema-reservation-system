package com.cinema.application.port.in.command;

import com.cinema.application.port.in.dto.ScreeningSeatDto;

public interface LockSeatUseCase {
    ScreeningSeatDto handle(LockSeatCommand cmd);
}
