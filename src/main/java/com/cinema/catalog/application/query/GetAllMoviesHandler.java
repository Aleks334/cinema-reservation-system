package com.cinema.catalog.application.query;

import com.cinema.shared.QueryHandler;
import com.cinema.catalog.application.query.dto.MovieDto;
import com.cinema.catalog.domain.port.MovieRepository;
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
