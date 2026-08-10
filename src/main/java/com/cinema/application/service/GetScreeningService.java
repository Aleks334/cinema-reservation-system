package com.cinema.application.service;

import com.cinema.application.mapping.ScreeningMapper;
import com.cinema.application.port.in.query.GetScreeningHandler;
import com.cinema.application.port.in.dto.ScreeningDto;
import com.cinema.application.port.in.query.GetScreeningQuery;
import com.cinema.application.port.out.ScreeningRepository;

import java.util.Objects;
import java.util.Optional;

public class GetScreeningService implements GetScreeningHandler {

    private final ScreeningRepository repository;

    public GetScreeningService(ScreeningRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ScreeningDto> execute(GetScreeningQuery query) {
        Objects.requireNonNull(query.screeningId());
        return repository.findById(query.screeningId())
                .map(ScreeningMapper::toDto);
    }
}
