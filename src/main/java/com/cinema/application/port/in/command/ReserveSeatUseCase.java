package com.cinema.application.port.in.command;

import com.cinema.application.port.in.dto.ScreeningSeatDto;

public interface ReserveSeatUseCase {
    ScreeningSeatDto handle(ReserveSeatCommand cmd);
}
