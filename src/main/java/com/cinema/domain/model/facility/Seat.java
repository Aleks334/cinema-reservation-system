package com.cinema.domain.model.facility;

import java.util.Objects;
import java.util.regex.Pattern;

public record Seat(String row, String number, SeatType seatType) {

    private static final Pattern ROW_PATTERN = Pattern.compile("^[A-Z]$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[1-9][0-9]*$");

    public Seat {
        Objects.requireNonNull(seatType, "Seat type cannot be null");
        validateRow(row);
        validateNumber(number);
    }

    private static void validateRow(String row) {
        Objects.requireNonNull(row, "Seat row cannot be null");
        if (row.isBlank()) {
            throw new IllegalArgumentException("Seat row cannot be empty");
        }
        if (!ROW_PATTERN.matcher(row).matches()) {
            throw new IllegalArgumentException("Seat row must be one uppercase letter");
        }
    }

    private static void validateNumber(String number) {
        Objects.requireNonNull(number, "Seat number cannot be null");
        if (number.isBlank()) {
            throw new IllegalArgumentException("Seat number cannot be empty");
        }
        if (!NUMBER_PATTERN.matcher(number).matches()) {
            throw new IllegalArgumentException("Seat number must be a positive non-zero number");
        }
    }

    public String getSeatPosition() {
        return row + number;
    }
}
