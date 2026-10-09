package com.cinema.ticketing.application.query.port;

import com.cinema.ticketing.application.query.dto.ScreeningDto;

import java.util.List;
import java.util.Optional;

public interface ScreeningReadRepository {
    Optional<ScreeningDto> findById(String screeningId);
    List<ScreeningDto> findByMovieId(String movieId);
}