package com.cinema.application.queries;

import com.cinema.application.dto.MovieDto;
import com.cinema.application.ports.in.Query;
import com.cinema.domain.model.catalog.MovieId;

import java.util.Optional;

public record GetMovieQuery(MovieId movieId) implements Query<Optional<MovieDto>> { }
