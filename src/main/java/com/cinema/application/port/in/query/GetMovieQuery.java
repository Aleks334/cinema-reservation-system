package com.cinema.application.port.in.query;

import com.cinema.application.port.in.dto.MovieDto;
import com.cinema.domain.model.vo.MovieId;
import java.util.Optional;

public interface GetMovieQuery {
    Optional<MovieDto> getMovie(MovieId movieId);
}
