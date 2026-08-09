package com.cinema.application.port.out;

import com.cinema.domain.model.Cinema;
import com.cinema.domain.model.vo.CinemaId;
import java.util.Optional;

public interface LoadCinemaPort {
    Optional<Cinema> loadById(CinemaId cinemaId);
}
