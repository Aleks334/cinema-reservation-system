package com.cinema.domain.model.fixtures;

import com.cinema.domain.model.facility.Room;
import com.cinema.domain.model.facility.RoomId;
import com.cinema.domain.model.facility.Seat;

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

