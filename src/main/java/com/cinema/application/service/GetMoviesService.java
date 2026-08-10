package com.cinema.application.service;

import com.cinema.application.mapping.MovieMapper;
import com.cinema.application.port.in.query.GetMoviesHandler;
import com.cinema.application.port.in.dto.MovieDto;
import com.cinema.application.port.out.MovieRepository;

import java.util.List;

public class GetMoviesService implements GetMoviesHandler {

    private final MovieRepository repository;

    public GetMoviesService(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MovieDto> execute() {
        return repository.getAll().stream()
                .map(MovieMapper::toDto)
                .toList();
    }
}
