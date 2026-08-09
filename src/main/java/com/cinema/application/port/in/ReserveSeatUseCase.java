package com.cinema.application.port.in;

import com.cinema.application.port.in.dto.ScreeningSeatDto;
import com.cinema.domain.model.vo.ScreeningId;
import com.cinema.domain.model.vo.ScreeningSeatId;

public interface ReserveSeatUseCase {
    ScreeningSeatDto reserveSeat(ScreeningId screeningId, ScreeningSeatId seatId);
}
