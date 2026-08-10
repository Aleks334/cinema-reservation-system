package com.cinema.application.port.in.query;

import com.cinema.domain.model.vo.MovieId;

public record GetScreeningsForMovieQuery(MovieId movieId) {
}
