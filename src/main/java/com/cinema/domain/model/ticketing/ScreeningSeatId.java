package com.cinema.domain.model.ticketing;

import java.util.Objects;
import java.util.UUID;

public record ScreeningSeatId(UUID value) {
    public ScreeningSeatId {
        Objects.requireNonNull(value, "ScreeningSeatId value cannot be null");
    }

    public static ScreeningSeatId generate() {
        return new ScreeningSeatId(UUID.randomUUID());
    }

    public static ScreeningSeatId from(String value) {
        Objects.requireNonNull(value, "ScreeningSeatId string value cannot be null");
        return new ScreeningSeatId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
