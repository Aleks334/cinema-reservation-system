package com.cinema.application.service;

import com.cinema.application.mapping.MovieMapper;
import com.cinema.application.port.in.query.GetMovieHandler;
import com.cinema.application.port.in.dto.MovieDto;
import com.cinema.application.port.out.MovieRepository;
import com.cinema.domain.model.vo.MovieId;

import java.util.Objects;
import java.util.Optional;

public class GetMovieService implements GetMovieHandler {

    private final MovieRepository repository;

    public GetMovieService(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<MovieDto> getMovie(MovieId movieId) {
        Objects.requireNonNull(movieId);
        return repository.findById(movieId)
                .map(MovieMapper::toDto);
    }
}
