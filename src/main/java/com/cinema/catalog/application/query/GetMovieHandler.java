package com.cinema.catalog.application.query;

import com.cinema.catalog.application.query.dto.MovieDto;
import com.cinema.catalog.application.query.port.MovieReadRepository;
import com.cinema.catalog.domain.exception.NoSuchMovieFoundException;
import com.cinema.shared.QueryHandler;
import com.google.inject.Inject;

import java.util.Objects;

public class GetMovieHandler implements QueryHandler<GetMovieQuery, MovieDto> {

    private final MovieReadRepository repository;

    @Inject
    public GetMovieHandler(MovieReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public MovieDto handle(GetMovieQuery query) {
        Objects.requireNonNull(query.movieId());

        return repository.findById(query.movieId())
                .orElseThrow(() -> new NoSuchMovieFoundException(
                        "Movie with ID " + query.movieId() + " not found"
                ));
    }
}
