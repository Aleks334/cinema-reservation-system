package com.cinema.application.service;

import com.cinema.application.mapping.MovieMapper;
import com.cinema.application.port.in.query.GetMovieHandler;
import com.cinema.application.port.in.dto.MovieDto;
import com.cinema.application.port.in.query.GetMovieQuery;
import com.cinema.application.port.out.MovieRepository;

import java.util.Objects;
import java.util.Optional;

public class GetMovieService implements GetMovieHandler {

    private final MovieRepository repository;

    public GetMovieService(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<MovieDto> execute(GetMovieQuery query) {
        Objects.requireNonNull(query.movieId());

        return repository.findById(query.movieId())
                .map(MovieMapper::toDto);
    }
}
