package com.cinema.application.queries;

import com.cinema.application.dto.MovieDto;
import com.cinema.application.ports.in.QueryHandler;
import com.cinema.application.ports.out.MovieRepository;
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
