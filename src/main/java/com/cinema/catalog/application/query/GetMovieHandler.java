package com.cinema.catalog.application.query;

import com.cinema.shared.QueryHandler;
import com.cinema.catalog.application.query.dto.MovieDto;
import com.cinema.catalog.domain.port.MovieRepository;
import com.google.inject.Inject;

import java.util.Objects;
import java.util.Optional;

public class GetMovieHandler implements QueryHandler<GetMovieQuery, Optional<MovieDto>> {

    private final MovieRepository repository;

    @Inject
    public GetMovieHandler(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<MovieDto> handle(GetMovieQuery query) {
        Objects.requireNonNull(query.movieId());

        return repository.findById(query.movieId())
                .map(MovieDto::of);
    }
}
