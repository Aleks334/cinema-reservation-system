package com.cinema.facility.domain.fixtures;

import com.cinema.facility.domain.model.Room;
import com.cinema.facility.domain.model.RoomId;
import com.cinema.facility.domain.model.Seat;

import java.util.List;

public class RoomFixture {

    public static Room room(RoomId id, String number, int rows, int seatsPerRow) {
        List<Seat> seats = SeatFixture.basicSeats(rows, seatsPerRow);
        return new Room(id, number, seats);
    }

    public static Room roomWithSeats(RoomId id, String number, List<Seat> seats) {
        return new Room(id, number, seats);
    }
}

