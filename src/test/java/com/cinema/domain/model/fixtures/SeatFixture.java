package com.cinema.domain.model.fixtures;

import com.cinema.facility.domain.model.SeatType;
import com.cinema.facility.domain.model.Seat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SeatFixture {

    public static Seat seat(String row, String number, SeatType type) {
        return new Seat(row, number, type);
    }

    public static List<Seat> seatsForRow(String row, int count, SeatType type) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> new Seat(row, String.valueOf(i), type))
                .toList();
    }

    public static List<Seat> basicSeats(int rows, int seatsPerRow) {
        List<Seat> seats = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            char rowLetter = (char) ('A' + r);
            seats.addAll(seatsForRow(String.valueOf(rowLetter), seatsPerRow, SeatType.BASIC));
        }
        return seats;
    }
}

