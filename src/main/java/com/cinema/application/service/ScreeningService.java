package com.cinema.application.service;

import com.cinema.application.port.in.GetMovieQuery;
import com.cinema.application.port.in.GetMoviesQuery;
import com.cinema.application.port.in.GetScreeningQuery;
import com.cinema.application.port.in.GetScreeningsForMovieQuery;
import com.cinema.application.port.in.LockSeatUseCase;
import com.cinema.application.port.in.ReserveSeatUseCase;
import com.cinema.application.port.in.dto.MovieDto;
import com.cinema.application.port.in.dto.ScreeningDto;
import com.cinema.application.port.in.dto.ScreeningSeatDto;
import com.cinema.application.port.out.LoadMoviePort;
import com.cinema.application.port.out.LoadScreeningPort;
import com.cinema.application.port.out.SaveScreeningPort;
import com.cinema.domain.exception.ScreeningNotFoundException;
import com.cinema.domain.model.Movie;
import com.cinema.domain.model.vo.MovieId;
import com.cinema.domain.model.Screening;
import com.cinema.domain.model.vo.ScreeningId;
import com.cinema.domain.model.ScreeningSeat;
import com.cinema.domain.model.vo.ScreeningSeatId;

import java.time.Clock;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ScreeningService implements GetScreeningQuery, GetScreeningsForMovieQuery,
        GetMoviesQuery, GetMovieQuery, LockSeatUseCase, ReserveSeatUseCase {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    private final LoadMoviePort loadMoviePort;
    private final LoadScreeningPort loadScreeningPort;
    private final SaveScreeningPort saveScreeningPort;

    private final java.time.Duration lockTimeout;
    private final Clock clock;

    public ScreeningService(LoadMoviePort loadMoviePort,
                            LoadScreeningPort loadScreeningPort,
                            SaveScreeningPort saveScreeningPort,
                            java.time.Duration lockTimeout,
                            Clock clock) {
        this.loadMoviePort = Objects.requireNonNull(loadMoviePort);
        this.loadScreeningPort = Objects.requireNonNull(loadScreeningPort);
        this.saveScreeningPort = Objects.requireNonNull(saveScreeningPort);
        this.lockTimeout = Objects.requireNonNull(lockTimeout);
        this.clock = clock;
    }

    @Override
    public Optional<ScreeningDto> getScreening(ScreeningId screeningId) {
        Objects.requireNonNull(screeningId);
        return loadScreeningPort.loadById(screeningId)
                .map(this::mapToScreeningDto);
    }

    @Override
    public List<ScreeningDto> getScreeningsForMovie(MovieId movieId) {
        Objects.requireNonNull(movieId);
        return loadScreeningPort.loadByMovieId(movieId).stream()
                .map(this::mapToScreeningDto)
                .toList();
    }

    @Override
    public List<MovieDto> getMovies() {
        return loadMoviePort.loadAll().stream()
                .map(this::mapToMovieDto)
                .toList();
    }

    @Override
    public Optional<MovieDto> getMovie(MovieId movieId) {
        Objects.requireNonNull(movieId);
        return loadMoviePort.loadById(movieId)
                .map(this::mapToMovieDto);
    }

    @Override
    public ScreeningSeatDto lockSeat(ScreeningId screeningId, ScreeningSeatId seatId) {
        Objects.requireNonNull(screeningId);
        Objects.requireNonNull(seatId);

        Screening screening = loadScreeningPort.loadById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(
                        "Screening with ID " + screeningId + " not found"
                ));

        screening.lockSeat(seatId, lockTimeout, clock);
        saveScreeningPort.save(screening);
        screening.clearModifiedSeats();

        return screening.getScreeningSeats().stream()
                .filter(s -> s.getId().equals(seatId))
                .map(this::mapToScreeningSeatDto)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public ScreeningSeatDto reserveSeat(ScreeningId screeningId, ScreeningSeatId seatId) {
        Objects.requireNonNull(screeningId);
        Objects.requireNonNull(seatId);

        Screening screening = loadScreeningPort.loadById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(
                        "Screening with ID " + screeningId + " not found"
                ));

        screening.reserveSeat(seatId, lockTimeout, clock);
        saveScreeningPort.save(screening);
        screening.clearModifiedSeats();

        return screening.getScreeningSeats().stream()
                .filter(s -> s.getId().equals(seatId))
                .map(this::mapToScreeningSeatDto)
                .findFirst()
                .orElseThrow();
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

    private MovieDto mapToMovieDto(Movie movie) {
        return new MovieDto(
                movie.getId().toString(),
                movie.getTitle(),
                movie.getDirector().getFullName(),
                movie.getDescription(),
                movie.getGenre().toString(),
                movie.getDuration().totalMinutes()
        );
    }
}
