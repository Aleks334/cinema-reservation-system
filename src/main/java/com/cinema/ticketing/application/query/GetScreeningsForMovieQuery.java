package com.cinema.ticketing.application.query;

import com.cinema.ticketing.application.query.dto.ScreeningDto;
import com.cinema.shared.Query;

import java.util.List;

public record GetScreeningsForMovieQuery(String movieId) implements Query<List<ScreeningDto>> { }
