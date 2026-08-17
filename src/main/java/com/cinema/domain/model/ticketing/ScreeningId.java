package com.cinema.domain.model.ticketing;

import java.util.Objects;
import java.util.UUID;

public record ScreeningId(UUID value) {
    public ScreeningId {
        Objects.requireNonNull(value, "ScreeningId value cannot be null");
    }

    public static ScreeningId generate() {
        return new ScreeningId(UUID.randomUUID());
    }

    public static ScreeningId from(String value) {
        Objects.requireNonNull(value, "ScreeningId string value cannot be null");
        return new ScreeningId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
