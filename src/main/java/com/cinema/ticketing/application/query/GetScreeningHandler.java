package com.cinema.ticketing.application.query;

import com.cinema.ticketing.application.query.dto.ScreeningDto;
import com.cinema.shared.QueryHandler;
import com.cinema.ticketing.domain.exception.ScreeningNotFoundException;
import com.cinema.ticketing.domain.model.ScreeningId;
import com.cinema.ticketing.domain.port.ScreeningRepository;
import com.google.inject.Inject;

import java.util.Objects;

public class GetScreeningHandler implements QueryHandler<GetScreeningQuery, ScreeningDto> {

    private final ScreeningRepository repository;

    @Inject
    public GetScreeningHandler(ScreeningRepository repository) {
        this.repository = repository;
    }

    @Override
    public ScreeningDto handle(GetScreeningQuery query) {
        Objects.requireNonNull(query.screeningId());
        return repository.findById(ScreeningId.from(query.screeningId()))
                .map(ScreeningDto::of)
                .orElseThrow(() -> new ScreeningNotFoundException(
                        "Screening with ID " + query.screeningId() + " not found"
                ));
    }
}
