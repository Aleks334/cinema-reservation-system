package com.cinema.ticketing.application.query.dto;

import com.cinema.ticketing.domain.model.ScreeningSeat;

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
                seat.getSeat().type(),
                seat.getStatus().name()
        );
    }
}