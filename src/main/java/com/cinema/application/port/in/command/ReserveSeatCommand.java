package com.cinema.application.port.in.command;

import com.cinema.domain.model.ticketing.ScreeningId;
import com.cinema.domain.model.ticketing.ScreeningSeatId;

public record ReserveSeatCommand(ScreeningId screeningId, ScreeningSeatId screeningSeatId) implements Command { }
