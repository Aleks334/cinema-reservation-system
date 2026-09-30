package com.cinema.catalog.application.query;

import com.cinema.shared.Query;
import com.cinema.catalog.application.query.dto.MovieDto;
import com.cinema.catalog.domain.model.MovieId;

import java.util.Optional;

public record GetMovieQuery(MovieId movieId) implements Query<Optional<MovieDto>> { }
