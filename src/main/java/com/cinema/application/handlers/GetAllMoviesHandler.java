package com.cinema.application.handlers;

import com.cinema.application.dto.MovieDto;
import com.cinema.application.port.in.query.GetAllMoviesQuery;
import com.cinema.application.port.in.query.QueryHandler;
import com.cinema.application.port.out.MovieRepository;

import java.util.List;

public class GetAllMoviesHandler implements QueryHandler<GetAllMoviesQuery, List<MovieDto>> {

    private final MovieRepository repository;

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
