package com.cinema.ticketing.application.query.dto;

import com.cinema.ticketing.domain.model.Screening;

import java.util.List;

import static com.cinema.shared.Formatters.DATE_FORMATTER;
import static com.cinema.shared.Formatters.TIME_FORMATTER;

public record ScreeningDto(
        String id,
        String movieId,
        String date,
        String startTime,
        List<ScreeningSeatDto> screeningSeats
) {

    public static ScreeningDto of(Screening screening) {
        return new ScreeningDto(
                screening.getId().toString(),
                screening.getMovieId().toString(),
                screening.getStartDateTime().format(DATE_FORMATTER),
                screening.getStartDateTime().format(TIME_FORMATTER),
                screening.getScreeningSeats().stream()
                        .map(ScreeningSeatDto::of)
                        .toList()
        );
    }
}
