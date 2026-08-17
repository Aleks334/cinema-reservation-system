package com.cinema.application.port.in.query;

import com.cinema.application.dto.ScreeningDto;
import com.cinema.domain.model.catalog.MovieId;

import java.util.List;

public record GetScreeningsForMovieQuery(MovieId movieId) implements Query<List<ScreeningDto>> { }
