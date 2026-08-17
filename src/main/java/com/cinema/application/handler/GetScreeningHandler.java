package com.cinema.application.handler;

import com.cinema.application.port.in.dto.ScreeningDto;
import com.cinema.application.port.in.query.GetScreeningQuery;
import com.cinema.application.port.in.query.QueryHandler;
import com.cinema.application.port.out.ScreeningRepository;

import java.util.Objects;
import java.util.Optional;

public class GetScreeningHandler implements QueryHandler<GetScreeningQuery, Optional<ScreeningDto>> {

    private final ScreeningRepository repository;

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
