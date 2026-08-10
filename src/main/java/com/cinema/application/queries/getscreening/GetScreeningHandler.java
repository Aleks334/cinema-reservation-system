package com.cinema.application.queries.getscreening;

import com.cinema.application.mappers.ScreeningMapper;
import com.cinema.application.port.in.GetScreeningQuery;
import com.cinema.application.port.in.dto.ScreeningDto;
import com.cinema.application.port.out.ScreeningRepository;
import com.cinema.domain.model.vo.ScreeningId;

import java.util.Objects;
import java.util.Optional;

public class GetScreeningHandler implements GetScreeningQuery {

    private final ScreeningRepository repository;

    public GetScreeningHandler(ScreeningRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ScreeningDto> getScreening(ScreeningId screeningId) {
        Objects.requireNonNull(screeningId);
        return repository.findById(screeningId)
                .map(ScreeningMapper::toDto);
    }
}
