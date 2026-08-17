package com.cinema.application.handler;

import com.cinema.application.dto.ScreeningDto;
import com.cinema.application.port.in.query.GetScreeningsForMovieQuery;
import com.cinema.application.port.in.query.QueryHandler;
import com.cinema.application.port.out.ScreeningRepository;

import java.util.List;
import java.util.Objects;

public class GetScreeningsForMovieHandler implements QueryHandler<GetScreeningsForMovieQuery, List<ScreeningDto>> {

    private final ScreeningRepository repository;

    public GetScreeningsForMovieHandler(ScreeningRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ScreeningDto> handle(GetScreeningsForMovieQuery query) {
        Objects.requireNonNull(query.movieId());
        return repository.findByMovieId(query.movieId()).stream()
                .map(ScreeningDto::of)
                .toList();
    }
}
