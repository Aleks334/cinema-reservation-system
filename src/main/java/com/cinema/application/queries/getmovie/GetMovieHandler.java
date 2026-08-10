package com.cinema.application.queries.getmovie;

import com.cinema.application.port.in.GetMovieQuery;
import com.cinema.application.port.in.dto.MovieDto;
import com.cinema.application.port.out.MovieRepository;
import com.cinema.domain.model.Movie;
import com.cinema.domain.model.vo.MovieId;

import java.util.Objects;
import java.util.Optional;

public class GetMovieHandler implements GetMovieQuery {

    private final MovieRepository repository;

    public GetMovieHandler(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<MovieDto> getMovie(MovieId movieId) {
        Objects.requireNonNull(movieId);
        return repository.findById(movieId)
                .map(this::mapToMovieDto);
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
