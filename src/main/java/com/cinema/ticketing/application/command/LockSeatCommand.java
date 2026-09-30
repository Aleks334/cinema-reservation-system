package com.cinema.ticketing.application.command;

import com.cinema.shared.Command;
import com.cinema.ticketing.domain.model.ScreeningId;
import com.cinema.ticketing.domain.model.ScreeningSeatId;

public record LockSeatCommand(ScreeningId screeningId, ScreeningSeatId screeningSeatId) implements Command { }
