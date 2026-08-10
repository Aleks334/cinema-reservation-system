package com.cinema.application.mapping;

import com.cinema.application.port.in.dto.ScreeningDto;
import com.cinema.domain.model.Screening;

import static com.cinema.application.mapping.Formatters.DATE_FORMATTER;
import static com.cinema.application.mapping.Formatters.TIME_FORMATTER;

public final class ScreeningMapper {

    private ScreeningMapper() {}

    public static ScreeningDto toDto(Screening screening) {
        return new ScreeningDto(
                screening.getId().toString(),
                screening.getMovieId().toString(),
                screening.getStartDateTime().format(DATE_FORMATTER),
                screening.getStartDateTime().format(TIME_FORMATTER),
                screening.getScreeningSeats().stream()
                        .map(ScreeningSeatMapper::toDto)
                        .toList()
        );
    }
}
