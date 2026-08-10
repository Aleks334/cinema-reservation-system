package com.cinema.application.mapping;

import com.cinema.application.port.in.dto.MovieDto;
import com.cinema.domain.model.Movie;

public final class MovieMapper {

    private MovieMapper() {}

    public static MovieDto toDto(Movie movie) {
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
