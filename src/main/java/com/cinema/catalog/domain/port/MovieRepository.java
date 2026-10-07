package com.cinema.catalog.domain.port;

import com.cinema.catalog.domain.model.Movie;
import com.cinema.catalog.domain.model.MovieId;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {
    Optional<Movie> findById(MovieId movieId);
    List<Movie> getAll();
}
