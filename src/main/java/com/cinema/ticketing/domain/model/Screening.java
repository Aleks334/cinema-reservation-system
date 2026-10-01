package com.cinema.ticketing.domain.model;

import com.cinema.ticketing.domain.exception.SeatNotAvailableException;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Screening {
    private final ScreeningId id;
    private final MovieId movieId;
    private final RoomId roomId;
    private final ZonedDateTime startDateTime;

    private final Map<ScreeningSeatId, ScreeningSeat> screeningSeats;
    private final Set<ScreeningSeatId> modifiedSeatIds = new HashSet<>();

    public Screening(ScreeningId id, MovieId movieId, RoomId roomId,
                     ZonedDateTime startDateTime, List<ScreeningSeat> screeningSeats) {

        this.id = Objects.requireNonNull(id, "Screening ID cannot be null");
        this.movieId = Objects.requireNonNull(movieId, "Movie ID cannot be null");
        this.roomId = Objects.requireNonNull(roomId, "Room ID cannot be null");
        this.startDateTime = Objects.requireNonNull(startDateTime, "Start date time cannot be null");

        this.screeningSeats = Objects.requireNonNull(screeningSeats, "Screening seats cannot be null")
                .stream()
                .collect(Collectors.toMap(ScreeningSeat::getId, Function.identity()));
    }

    public void lockSeat(ScreeningSeatId seatId, Duration lockTimeout, Instant now) {
        ScreeningSeat seat = getSeatOrThrow(seatId);

        if (seat.isLocked() && seat.isLockExpired(lockTimeout, now)) {
            seat.release();
        }

        seat.lock(now);
        markAsDirty(seatId);
    }

    public void reserveSeat(ScreeningSeatId seatId, Duration lockTimeout, Instant now) {
        ScreeningSeat seat = getSeatOrThrow(seatId);

        seat.reserve(lockTimeout, now);
        markAsDirty(seatId);
    }

    public void releaseSeat(ScreeningSeatId seatId) {
        ScreeningSeat seat = getSeatOrThrow(seatId);

        seat.release();
        markAsDirty(seatId);
    }

    public boolean isSeatAvailable(ScreeningSeatId seatId) {
        ScreeningSeat seat = screeningSeats.get(seatId);
        return seat != null && seat.isAvailable();
    }

    private ScreeningSeat getSeatOrThrow(ScreeningSeatId seatId) {
        ScreeningSeat seat = screeningSeats.get(seatId);
        if (seat == null) {
            throw new SeatNotAvailableException("Seat with ID " + seatId + " not found in screening " + id);
        }
        return seat;
    }

    private void markAsDirty(ScreeningSeatId seatId) {
        this.modifiedSeatIds.add(seatId);
    }

    public ScreeningId getId() {
        return id;
    }

    public MovieId getMovieId() {
        return movieId;
    }

    public RoomId getRoomId() {
        return roomId;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public List<ScreeningSeat> getAvailableSeats() {
        return screeningSeats.values().stream()
                .filter(ScreeningSeat::isAvailable)
                .toList();
    }

    public List<ScreeningSeat> getModifiedSeats() {
        return modifiedSeatIds.stream()
                .map(screeningSeats::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public void clearModifiedSeats() {
        this.modifiedSeatIds.clear();
    }

    public List<ScreeningSeat> getScreeningSeats() {
        return List.copyOf(screeningSeats.values());
    }
}