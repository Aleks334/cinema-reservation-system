package com.cinema.application.port.in.query;

import com.cinema.application.port.in.dto.MovieDto;

import java.util.List;

public record GetAllMoviesQuery() implements Query<List<MovieDto>> { }
