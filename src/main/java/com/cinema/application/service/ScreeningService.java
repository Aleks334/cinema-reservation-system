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
import com.cinema.application.port.out.MovieRepository;
import com.cinema.application.port.out.ScreeningRepository;
import com.cinema.domain.exception.ScreeningNotFoundException;
import com.cinema.domain.model.Movie;
import com.cinema.domain.model.vo.MovieId;
import com.cinema.domain.model.Screening;
import com.cinema.domain.model.vo.ScreeningId;
import com.cinema.domain.model.ScreeningSeat;
import com.cinema.domain.model.vo.ScreeningSeatId;

import java.time.Clock;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ScreeningService implements
        GetMoviesQuery, GetMovieQuery, GetScreeningsForMovieQuery,
        GetScreeningQuery,
        LockSeatUseCase, ReserveSeatUseCase {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;

    private final Duration lockTimeout;
    private final Clock clock;

    public ScreeningService(MovieRepository movieRepository,
                            ScreeningRepository screeningRepository,
                            Duration lockTimeout,
                            Clock clock) {
        this.movieRepository = Objects.requireNonNull(movieRepository);
        this.screeningRepository = Objects.requireNonNull(screeningRepository);
        this.lockTimeout = Objects.requireNonNull(lockTimeout);
        this.clock = clock;
    }

    @Override
    public Optional<ScreeningDto> getScreening(ScreeningId screeningId) {
        Objects.requireNonNull(screeningId);
        return screeningRepository.findById(screeningId)
                .map(this::mapToScreeningDto);
    }

    @Override
    public List<ScreeningDto> getScreeningsForMovie(MovieId movieId) {
        Objects.requireNonNull(movieId);
        return screeningRepository.findByMovieId(movieId).stream()
                .map(this::mapToScreeningDto)
                .toList();
    }

    @Override
    public List<MovieDto> getMovies() {
        return movieRepository.getAll().stream()
                .map(this::mapToMovieDto)
                .toList();
    }

    @Override
    public Optional<MovieDto> getMovie(MovieId movieId) {
        Objects.requireNonNull(movieId);
        return movieRepository.findById(movieId)
                .map(this::mapToMovieDto);
    }

    @Override
    public ScreeningSeatDto lockSeat(ScreeningId screeningId, ScreeningSeatId seatId) {
        Objects.requireNonNull(screeningId);
        Objects.requireNonNull(seatId);

        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(
                        "Screening with ID " + screeningId + " not found"
                ));

        screening.lockSeat(seatId, lockTimeout, clock);
        screeningRepository.save(screening);
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

        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(
                        "Screening with ID " + screeningId + " not found"
                ));

        screening.reserveSeat(seatId, lockTimeout, clock);
        screeningRepository.save(screening);
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
