package com.cinema.application.queries.getscreeningsformovie;

import com.cinema.application.port.in.GetScreeningsForMovieQuery;
import com.cinema.application.port.in.dto.ScreeningDto;
import com.cinema.application.port.in.dto.ScreeningSeatDto;
import com.cinema.application.port.out.ScreeningRepository;
import com.cinema.domain.model.Screening;
import com.cinema.domain.model.ScreeningSeat;
import com.cinema.domain.model.vo.MovieId;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class GetScreeningsForMovieHandler implements GetScreeningsForMovieQuery {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    private final ScreeningRepository repository;

    public GetScreeningsForMovieHandler(ScreeningRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ScreeningDto> getScreeningsForMovie(MovieId movieId) {
        Objects.requireNonNull(movieId);
        return repository.findByMovieId(movieId).stream()
                .map(this::mapToScreeningDto)
                .toList();
    }

    private ScreeningDto mapToScreeningDto(Screening screening) {
        return new ScreeningDto(
                screening.getId().toString(),
                screening.getMovieId().toString(),
                screening.getStartDateTime().format(DATE_FORMATTER),
                screening.getStartDateTime().format(TIME_FORMATTER),
                screening.getScreeningSeats().stream()
                        .map(this::mapToScreeningSeatDto)
                        .toList()
        );
    }

    private ScreeningSeatDto mapToScreeningSeatDto(ScreeningSeat seat) {
        return new ScreeningSeatDto(
                seat.getId().toString(),
                seat.getSeat().row(),
                seat.getSeat().number(),
                seat.getSeat().seatType().name(),
                seat.getStatus().name()
        );
    }
}
