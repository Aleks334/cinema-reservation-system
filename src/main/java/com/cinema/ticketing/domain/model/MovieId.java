package com.cinema.ticketing.domain.model;

import java.util.Objects;
import java.util.UUID;

public record MovieId(UUID value) {
    public MovieId {
        Objects.requireNonNull(value, "MovieId value cannot be null");
    }

    public static MovieId generate() {
        return new MovieId(UUID.randomUUID());
    }

    public static MovieId from(String value) {
        Objects.requireNonNull(value, "MovieId string cannot be null");
        return new MovieId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}