package com.cinema.catalog.application.query;

import com.cinema.catalog.domain.exception.NoSuchMovieFoundException;
import com.cinema.catalog.domain.model.MovieId;
import com.cinema.shared.QueryHandler;
import com.cinema.catalog.application.query.dto.MovieDto;
import com.cinema.catalog.domain.port.MovieRepository;
import com.google.inject.Inject;

import java.util.Objects;

public class GetMovieHandler implements QueryHandler<GetMovieQuery, MovieDto> {

    private final MovieRepository repository;

    @Inject
    public GetMovieHandler(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public MovieDto handle(GetMovieQuery query) {
        Objects.requireNonNull(query.movieId());

        return repository.findById(MovieId.from(query.movieId()))
                .map(MovieDto::of)
                .orElseThrow(() -> new NoSuchMovieFoundException(
                        "Movie with ID " + query.movieId() + " not found"
                ));
    }
}
