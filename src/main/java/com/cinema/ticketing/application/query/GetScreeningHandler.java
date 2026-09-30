package com.cinema.ticketing.application.query;

import com.cinema.ticketing.application.query.dto.ScreeningDto;
import com.cinema.shared.QueryHandler;
import com.cinema.ticketing.domain.port.ScreeningRepository;
import com.google.inject.Inject;

import java.util.Objects;
import java.util.Optional;

public class GetScreeningHandler implements QueryHandler<GetScreeningQuery, Optional<ScreeningDto>> {

    private final ScreeningRepository repository;

    @Inject
    public GetScreeningHandler(ScreeningRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ScreeningDto> handle(GetScreeningQuery query) {
        Objects.requireNonNull(query.screeningId());
        return repository.findById(query.screeningId())
                .map(ScreeningDto::of);
    }
}
