package com.cinema.ticketing.application.query;

import com.cinema.ticketing.application.query.dto.ScreeningDto;
import com.cinema.shared.QueryHandler;
import com.cinema.ticketing.domain.port.ScreeningRepository;
import com.google.inject.Inject;

import java.util.List;
import java.util.Objects;

public class GetScreeningsForMovieHandler implements QueryHandler<GetScreeningsForMovieQuery, List<ScreeningDto>> {

    private final ScreeningRepository repository;

    @Inject
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
