package com.cinema.ticketing.domain.fixtures;

import com.cinema.ticketing.domain.model.MovieId;
import com.cinema.ticketing.domain.model.RoomId;
import com.cinema.ticketing.domain.model.Screening;
import com.cinema.ticketing.domain.model.ScreeningId;
import com.cinema.ticketing.domain.model.ScreeningSeat;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

public class ScreeningFixture {

    public static final Duration TIMEOUT = Duration.ofMinutes(10);
    public static final Instant NOW = Instant.parse("2026-01-05T12:00:00Z");

    public static Screening anyScreening(List<ScreeningSeat> seats) {
        return new Screening(
                ScreeningId.generate(),
                MovieId.generate(),
                RoomId.generate(),
                NOW.atZone(ZoneId.of("UTC")),
                seats
        );
    }
}