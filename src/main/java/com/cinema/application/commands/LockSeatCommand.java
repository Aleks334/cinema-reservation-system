package com.cinema.application.commands;

import com.cinema.application.port.in.Command;
import com.cinema.domain.model.ticketing.ScreeningId;
import com.cinema.domain.model.ticketing.ScreeningSeatId;

public record LockSeatCommand(ScreeningId screeningId, ScreeningSeatId screeningSeatId) implements Command { }
