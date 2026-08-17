package com.cinema.application.port.in.command;

import com.cinema.domain.model.ticketing.ScreeningId;
import com.cinema.domain.model.ticketing.ScreeningSeatId;

public record LockSeatCommand(ScreeningId screeningId, ScreeningSeatId screeningSeatId) implements Command { }
