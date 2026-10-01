package com.cinema.ticketing.application.command;

import com.cinema.shared.Command;

public record LockSeatCommand(String screeningId, String screeningSeatId) implements Command { }
