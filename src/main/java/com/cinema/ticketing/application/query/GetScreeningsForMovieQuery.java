package com.cinema.ticketing.application.query;

import com.cinema.ticketing.application.query.dto.ScreeningDto;
import com.cinema.shared.Query;
import com.cinema.catalog.domain.model.MovieId;

import java.util.List;

public record GetScreeningsForMovieQuery(MovieId movieId) implements Query<List<ScreeningDto>> { }
