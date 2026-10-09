package com.cinema.catalog.application.query.port;

import com.cinema.catalog.application.query.dto.MovieDto;

import java.util.List;
import java.util.Optional;

public interface MovieReadRepository {
    Optional<MovieDto> findById(String movieId);
    List<MovieDto> findAll();
}