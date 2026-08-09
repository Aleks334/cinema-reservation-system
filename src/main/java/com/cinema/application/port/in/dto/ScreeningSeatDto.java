package com.cinema.application.port.in.dto;

public record ScreeningSeatDto(
        String id,
        String row,
        String number,
        String seatType,
        String status
) {
}
