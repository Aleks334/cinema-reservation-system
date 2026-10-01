package com.cinema.facility.domain.port;

import com.cinema.facility.domain.model.Cinema;
import com.cinema.facility.domain.model.CinemaId;

import java.util.Optional;

public interface CinemaRepository {
    Optional<Cinema> findById(CinemaId cinemaId);
}
