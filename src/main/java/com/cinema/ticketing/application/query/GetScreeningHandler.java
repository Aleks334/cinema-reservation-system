package com.cinema.ticketing.application.query;

import com.cinema.shared.QueryHandler;
import com.cinema.ticketing.application.query.dto.ScreeningDto;
import com.cinema.ticketing.application.query.port.ScreeningReadRepository;
import com.cinema.ticketing.domain.exception.ScreeningNotFoundException;
import com.google.inject.Inject;

import java.util.Objects;

public class GetScreeningHandler implements QueryHandler<GetScreeningQuery, ScreeningDto> {

    private final ScreeningReadRepository repository;

    @Inject
    public GetScreeningHandler(ScreeningReadRepository repository) {
        this.repository = Objects.requireNonNull(repository, "ScreeningReadRepository cannot be null");
    }

    @Override
    public ScreeningDto handle(GetScreeningQuery query) {
        Objects.requireNonNull(query.screeningId(), "Screening ID cannot be null");
        return repository.findById(query.screeningId())
                .orElseThrow(() -> new ScreeningNotFoundException(
                        "Screening with ID " + query.screeningId() + " not found"
                ));
    }
}