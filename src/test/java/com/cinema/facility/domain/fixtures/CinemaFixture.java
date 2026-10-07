package com.cinema.facility.domain.fixtures;

import com.cinema.facility.domain.model.Cinema;
import com.cinema.facility.domain.model.Room;
import com.cinema.facility.domain.model.CinemaId;

import java.util.List;

public class CinemaFixture {

    public static Cinema cinema(CinemaId id, String name, List<Room> rooms) {
        return new Cinema(id, name, rooms);
    }
}

