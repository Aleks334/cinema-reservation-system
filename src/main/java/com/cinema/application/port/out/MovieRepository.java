package com.cinema.application.port.out;

import com.cinema.domain.model.catalog.Movie;
import com.cinema.domain.model.catalog.MovieId;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {
    Optional<Movie> findById(MovieId movieId);
    List<Movie> getAll();
}
