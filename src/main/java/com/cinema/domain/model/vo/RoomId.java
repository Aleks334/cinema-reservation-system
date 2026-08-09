package com.cinema.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record RoomId(UUID value) {
    public RoomId {
        Objects.requireNonNull(value, "RoomId value cannot be null");
    }

    public static RoomId generate() {
        return new RoomId(UUID.randomUUID());
    }

    public static RoomId from(String value) {
        Objects.requireNonNull(value, "RoomId string value cannot be null");
        return new RoomId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
