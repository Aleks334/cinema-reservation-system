package com.cinema.application.queries.getmovies;

import com.cinema.application.mappers.MovieMapper;
import com.cinema.application.port.in.GetMoviesQuery;
import com.cinema.application.port.in.dto.MovieDto;
import com.cinema.application.port.out.MovieRepository;

import java.util.List;

public class GetMoviesHandler implements GetMoviesQuery {

    private final MovieRepository repository;

    public GetMoviesHandler(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MovieDto> getMovies() {
        return repository.getAll().stream()
                .map(MovieMapper::toDto)
                .toList();
    }
}
