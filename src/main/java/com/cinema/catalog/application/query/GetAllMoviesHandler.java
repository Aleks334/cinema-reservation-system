package com.cinema.catalog.application.query;

import com.cinema.catalog.application.query.port.MovieReadRepository;
import com.cinema.shared.QueryHandler;
import com.cinema.catalog.application.query.dto.MovieDto;
import com.google.inject.Inject;

import java.util.List;

public class GetAllMoviesHandler implements QueryHandler<GetAllMoviesQuery, List<MovieDto>> {

    private final MovieReadRepository repository;

    @Inject
    public GetAllMoviesHandler(MovieReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MovieDto> handle(GetAllMoviesQuery query) {
        return repository.findAll();
    }
}
