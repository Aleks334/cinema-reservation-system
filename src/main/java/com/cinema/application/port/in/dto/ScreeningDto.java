package com.cinema.application.port.in.dto;

import java.util.List;

public record ScreeningDto(
        String id,
        String movieId,
        String date,
        String startTime,
        List<ScreeningSeatDto> screeningSeats
) {
}
