package com.cinema.application.dto;

import com.cinema.domain.model.ticketing.ScreeningSeat;

public record ScreeningSeatDto(
        String id,
        String row,
        String number,
        String seatType,
        String status
) {

    public static ScreeningSeatDto of(ScreeningSeat seat) {
        return new ScreeningSeatDto(
                seat.getId().toString(),
                seat.getSeat().row(),
                seat.getSeat().number(),
                seat.getSeat().seatType().name(),
                seat.getStatus().name()
        );
    }
}
