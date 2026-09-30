package com.cinema.domain.model.fixtures;

import com.cinema.ticketing.domain.model.ScreeningSeat;
import com.cinema.ticketing.domain.model.ScreeningSeatStatus;
import com.cinema.facility.domain.model.SeatType;
import com.cinema.ticketing.domain.model.ScreeningSeatId;
import com.cinema.facility.domain.model.Seat;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

public class ScreeningSeatFixture {

    public static final Duration TIMEOUT = Duration.ofMinutes(10);
    public static final Instant NOW = Instant.parse("2026-01-05T12:00:00Z");
    public static final Clock FIXED_CLOCK = Clock.fixed(NOW, ZoneId.of("UTC"));

    public static ScreeningSeat anyAvailableSeat(String row, String num) {
        return new ScreeningSeat(
                ScreeningSeatId.generate(),
                new Seat(row, num, SeatType.BASIC),
                ScreeningSeatStatus.AVAILABLE,
                null,
                1
        );
    }
}
