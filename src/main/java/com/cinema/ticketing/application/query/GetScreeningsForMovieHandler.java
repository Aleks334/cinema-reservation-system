package com.cinema.ticketing.application.query;

import com.cinema.shared.QueryHandler;
import com.cinema.ticketing.application.query.dto.ScreeningDto;
import com.cinema.ticketing.domain.model.MovieId;
import com.cinema.ticketing.domain.port.ScreeningRepository;
import com.google.inject.Inject;

import java.util.List;
import java.util.Objects;

public class GetScreeningsForMovieHandler implements QueryHandler<GetScreeningsForMovieQuery, List<ScreeningDto>> {

    private final ScreeningRepository repository;

    @Inject
    public GetScreeningsForMovieHandler(ScreeningRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public List<ScreeningDto> handle(GetScreeningsForMovieQuery query) {
        Objects.requireNonNull(query.movieId(), "Movie ID cannot be null");
        return repository.findByMovieId(MovieId.from(query.movieId())).stream()
                .map(ScreeningDto::of)
                .toList();
    }
}