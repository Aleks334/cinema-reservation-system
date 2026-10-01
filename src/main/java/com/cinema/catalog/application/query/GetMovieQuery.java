package com.cinema.catalog.application.query;

import com.cinema.shared.Query;
import com.cinema.catalog.application.query.dto.MovieDto;

public record GetMovieQuery(String movieId) implements Query<MovieDto> { }
