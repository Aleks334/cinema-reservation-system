package com.cinema.ticketing.domain.fixtures;

import com.cinema.ticketing.domain.model.ScreeningSeat;
import com.cinema.ticketing.domain.model.ScreeningSeatId;
import com.cinema.ticketing.domain.model.ScreeningSeatStatus;
import com.cinema.ticketing.domain.model.SeatInfo;

import java.time.Duration;
import java.time.Instant;

public class ScreeningSeatFixture {

    public static final Duration TIMEOUT = Duration.ofMinutes(10);
    public static final Instant NOW = Instant.parse("2026-01-05T12:00:00Z");

    public static ScreeningSeat anyAvailableSeat(String row, String num) {
        return new ScreeningSeat(
                ScreeningSeatId.generate(),
                new SeatInfo(row, num, "BASIC"),
                ScreeningSeatStatus.AVAILABLE,
                null,
                1
        );
    }

    public static ScreeningSeat anyAvailableSeat() {
        return anyAvailableSeat("A", "1");
    }
}