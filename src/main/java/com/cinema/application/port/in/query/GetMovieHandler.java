package com.cinema.application.port.in.query;

import com.cinema.application.port.in.dto.MovieDto;

import java.util.Optional;

public interface GetMovieHandler {
    Optional<MovieDto> execute(GetMovieQuery query);
}
