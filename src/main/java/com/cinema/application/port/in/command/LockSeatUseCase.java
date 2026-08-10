package com.cinema.application.port.in.command;

import com.cinema.application.port.in.dto.ScreeningSeatDto;
import com.cinema.domain.model.vo.ScreeningId;
import com.cinema.domain.model.vo.ScreeningSeatId;

public interface LockSeatUseCase {
    ScreeningSeatDto lockSeat(ScreeningId screeningId, ScreeningSeatId seatId);
}
