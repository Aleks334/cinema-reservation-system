package com.cinema.domain.model;

import com.cinema.domain.model.vo.CinemaId;
import com.cinema.domain.model.vo.RoomId;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Cinema {
    private final CinemaId id;
    private final String name;
    private final List<Room> rooms;

    public Cinema(CinemaId id, String name, List<Room> rooms) {
        this.id = Objects.requireNonNull(id, "Cinema ID cannot be null");
        this.name = Objects.requireNonNull(name, "Cinema name cannot be null");
        this.rooms = List.copyOf(Objects.requireNonNull(rooms, "Cinema rooms cannot be null"));

        if(name.isBlank()) {
            throw new IllegalArgumentException("Cinema name cannot be blank");
        }

        validateUniqueRoomNumbers(rooms);
    }

    public CinemaId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalCapacity() {
        return rooms.stream()
                .mapToInt(Room::getCapacity)
                .sum();
    }

    private static void validateUniqueRoomNumbers(List<Room> rooms) {
        long distinctRoomNumbersCount = rooms.stream()
                .map(Room::getNumber)
                .distinct()
                .count();

        if (distinctRoomNumbersCount != rooms.size()) {
            throw new IllegalArgumentException("Cinema cannot contain rooms with duplicate numbers");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cinema other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
