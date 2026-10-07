package com.cinema.catalog.application.query.dto;

import com.cinema.catalog.domain.model.Movie;

public record MovieDto(
        String id,
        String title,
        String directorFullName,
        String description,
        String genre,
        int durationMinutes
) {

    public static MovieDto of(Movie movie) {
        return new MovieDto(
                movie.getId().toString(),
                movie.getTitle(),
                movie.getDirector().getFullName(),
                movie.getDescription(),
                movie.getGenre().toString(),
                movie.getDuration().totalMinutes()
        );
    }
}
