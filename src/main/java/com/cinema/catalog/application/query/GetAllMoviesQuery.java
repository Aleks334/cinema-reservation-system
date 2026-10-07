package com.cinema.catalog.application.query;

import com.cinema.shared.Query;
import com.cinema.catalog.application.query.dto.MovieDto;

import java.util.List;

public record GetAllMoviesQuery() implements Query<List<MovieDto>> { }
