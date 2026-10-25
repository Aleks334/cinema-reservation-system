package com.cinema.ticketing.application.query;

import com.cinema.shared.QueryHandler;
import com.cinema.ticketing.application.query.dto.ScreeningDto;
import com.cinema.ticketing.application.query.port.ScreeningReadRepository;
import com.google.inject.Inject;

import java.util.List;
import java.util.Objects;

public class GetScreeningsForMovieHandler implements QueryHandler<GetScreeningsForMovieQuery, List<ScreeningDto>> {

    private final ScreeningReadRepository repository;

    @Inject
    public GetScreeningsForMovieHandler(ScreeningReadRepository repository) {
        this.repository = Objects.requireNonNull(repository, "ScreeningReadRepository cannot be null");
    }

    @Override
    public List<ScreeningDto> handle(GetScreeningsForMovieQuery query) {
        Objects.requireNonNull(query.movieId(), "Movie ID cannot be null");
        return repository.findByMovieId(query.movieId());
    }
}