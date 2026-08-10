package com.cinema.application.mappers;

import com.cinema.application.port.in.dto.ScreeningSeatDto;
import com.cinema.domain.model.ScreeningSeat;

public final class ScreeningSeatMapper {

    private ScreeningSeatMapper() {}

    public static ScreeningSeatDto toDto(ScreeningSeat seat) {
        return new ScreeningSeatDto(
                seat.getId().toString(),
                seat.getSeat().row(),
                seat.getSeat().number(),
                seat.getSeat().seatType().name(),
                seat.getStatus().name()
        );
    }
}
