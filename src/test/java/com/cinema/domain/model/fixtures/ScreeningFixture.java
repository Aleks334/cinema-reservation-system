package com.cinema.domain.model.fixtures;

import com.cinema.domain.model.Screening;
import com.cinema.domain.model.ScreeningSeat;
import com.cinema.domain.model.vo.MovieId;
import com.cinema.domain.model.vo.RoomId;
import com.cinema.domain.model.vo.ScreeningId;

import java.time.*;
import java.util.List;

public class ScreeningFixture {

    public static final Duration TIMEOUT = Duration.ofMinutes(10);
    public static final Instant NOW = Instant.parse("2026-01-05T12:00:00Z");
    public static final Clock FIXED_CLOCK = Clock.fixed(NOW, ZoneId.of("UTC"));


    public static Screening anyScreening(List<ScreeningSeat> seats) {
        return new Screening(
               ScreeningId.generate(),
                MovieId.generate(),
                RoomId.generate(),
                ZonedDateTime.now(),
                seats
        );
    }
}
