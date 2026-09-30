package com.cinema.application.queries;

import com.cinema.application.dto.MovieDto;
import com.cinema.application.port.in.QueryHandler;
import com.cinema.application.port.out.MovieRepository;
import com.google.inject.Inject;

import java.util.List;

public class GetAllMoviesHandler implements QueryHandler<GetAllMoviesQuery, List<MovieDto>> {

    private final MovieRepository repository;

    @Inject
    public GetAllMoviesHandler(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MovieDto> handle(GetAllMoviesQuery query) {
        return repository.getAll().stream()
                .map(MovieDto::of)
                .toList();
    }
}
