package com.cinema.catalog.application.query.port;

import com.cinema.catalog.application.query.dto.MovieDto;
import com.cinema.catalog.domain.model.MovieId;

import java.util.List;
import java.util.Optional;

public interface MovieReadRepository {
    Optional<MovieDto> findById(MovieId movieId);
    List<MovieDto> findAll();
}