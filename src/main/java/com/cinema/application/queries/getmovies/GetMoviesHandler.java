package com.cinema.application.queries.getmovies;

import com.cinema.application.port.in.GetMoviesQuery;
import com.cinema.application.port.in.dto.MovieDto;
import com.cinema.application.port.out.MovieRepository;
import com.cinema.domain.model.Movie;

import java.util.List;

public class GetMoviesHandler implements GetMoviesQuery {

    private final MovieRepository repository;

    public GetMoviesHandler(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MovieDto> getMovies() {
        return repository.getAll().stream()
                .map(this::mapToMovieDto)
                .toList();
    }

    private MovieDto mapToMovieDto(Movie movie) {
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
