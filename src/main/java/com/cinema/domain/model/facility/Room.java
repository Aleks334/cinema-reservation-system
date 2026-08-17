package com.cinema.domain.model.facility;

import com.cinema.domain.exception.NoSuchSeatFoundException;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public final class Room {
    private final RoomId id;
    private final String number;
    private final List<Seat> seats;

    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[1-9][0-9]*$");

    public Room(RoomId id, String number, List<Seat> seats) {
        this.id = Objects.requireNonNull(id, "Room ID cannot be null");
        this.seats = List.copyOf(Objects.requireNonNull(seats, "Room seats cannot be null"));

        if(seats.isEmpty()) {
            throw new IllegalArgumentException("Room must contain at least one seat");
        }

        validateUniqueSeatPositions(seats);

        this.number = Objects.requireNonNull(number, "Room number cannot be null");

        if(number.isBlank()) {
            throw new IllegalArgumentException("Room number cannot be blank");
        }

        if(!NUMBER_PATTERN.matcher(number).matches()) {
            throw new IllegalArgumentException(
                    "Room number must be a positive non-zero number, but got: '" + number + "'"
            );
        }
    }

    public RoomId getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Seat getSeatAtPosition(String seatPosition) {
        return seats.stream()
                .filter(seat -> seat.getSeatPosition().equals(seatPosition))
                .findFirst()
                .orElseThrow(() -> new NoSuchSeatFoundException(
                        "No seat found at position: " + seatPosition));
    }

    public int getCapacity() {
        return seats.size();
    }

    private static void validateUniqueSeatPositions(List<Seat> seats) {
        long distinctSeatsCount = seats.stream()
                .map(Seat::getSeatPosition)
                .distinct()
                .count();

        if (distinctSeatsCount != seats.size()) {
            throw new IllegalArgumentException("Room cannot contain duplicate seat positions");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
