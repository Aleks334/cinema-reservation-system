package com.cinema.domain.model.fixtures;

import com.cinema.domain.model.facility.Cinema;
import com.cinema.domain.model.facility.Room;
import com.cinema.domain.model.facility.CinemaId;

import java.util.List;

public class CinemaFixture {

    public static Cinema cinema(CinemaId id, String name, List<Room> rooms) {
        return new Cinema(id, name, rooms);
    }
}

