package com.cinema.application.ports.out;

import com.cinema.domain.model.facility.Cinema;
import com.cinema.domain.model.facility.CinemaId;

import java.util.Optional;

public interface CinemaRepository {
    Optional<Cinema> findById(CinemaId cinemaId);
}
