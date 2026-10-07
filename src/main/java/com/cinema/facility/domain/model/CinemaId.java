package com.cinema.facility.domain.model;

import java.util.Objects;
import java.util.UUID;

public record CinemaId(UUID value) {
    public CinemaId {
        Objects.requireNonNull(value, "CinemaId value cannot be null");
    }

    public static CinemaId generate() {
        return new CinemaId(UUID.randomUUID());
    }

    public static CinemaId from(String value) {
        Objects.requireNonNull(value, "CinemaId string value cannot be null");
        return new CinemaId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
