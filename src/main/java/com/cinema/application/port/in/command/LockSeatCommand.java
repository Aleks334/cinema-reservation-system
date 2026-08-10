package com.cinema.application.port.in.command;

import com.cinema.domain.model.vo.ScreeningId;
import com.cinema.domain.model.vo.ScreeningSeatId;

public record LockSeatCommand(ScreeningId screeningId, ScreeningSeatId screeningSeatId) { }
