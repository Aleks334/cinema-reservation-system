package com.cinema.ticketing.domain.model;

import java.util.Objects;

public record SeatInfo(String row, String number, String type) {
    public SeatInfo {
        Objects.requireNonNull(type, "Seat type cannot be null");
    }

    public String getSeatPosition() {
        return row + number;
    }
}