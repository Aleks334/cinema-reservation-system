package com.cinema.application.queries;

import com.cinema.application.dto.MovieDto;
import com.cinema.application.ports.in.Query;

import java.util.List;

public record GetAllMoviesQuery() implements Query<List<MovieDto>> { }
