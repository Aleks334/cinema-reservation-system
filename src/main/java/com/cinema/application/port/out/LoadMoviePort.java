package com.cinema.application.port.out;

import com.cinema.domain.model.Movie;
import com.cinema.domain.model.vo.MovieId;
import java.util.List;
import java.util.Optional;

public interface LoadMoviePort {
    Optional<Movie> loadById(MovieId movieId);

    List<Movie> loadAll();
}
