package com.cinema.domain.model.fixtures;

import com.cinema.domain.model.Cinema;
import com.cinema.domain.model.Room;
import com.cinema.domain.model.vo.CinemaId;
import com.cinema.domain.model.vo.RoomId;

import java.util.List;
import java.util.stream.IntStream;

public class CinemaFixture {

    public static Cinema cinema(CinemaId id, String name, List<Room> rooms) {
        return new Cinema(id, name, rooms);
    }
}

