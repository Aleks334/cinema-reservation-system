package com.cinema.application.handler;

import com.cinema.application.mapping.MovieMapper;
import com.cinema.application.port.in.dto.MovieDto;
import com.cinema.application.port.in.query.GetMovieQuery;
import com.cinema.application.port.in.query.QueryHandler;
import com.cinema.application.port.out.MovieRepository;

import java.util.Objects;
import java.util.Optional;

public class GetMovieHandler implements QueryHandler<GetMovieQuery, Optional<MovieDto>> {

    private final MovieRepository repository;

    public GetMovieHandler(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<MovieDto> handle(GetMovieQuery query) {
        Objects.requireNonNull(query.movieId());

        return repository.findById(query.movieId())
                .map(MovieMapper::toDto);
    }
}
