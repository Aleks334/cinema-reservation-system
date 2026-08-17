package com.cinema.application.port.in.query;

import com.cinema.application.dto.ScreeningDto;
import com.cinema.domain.model.ticketing.ScreeningId;

import java.util.Optional;

public record GetScreeningQuery(ScreeningId screeningId) implements Query<Optional<ScreeningDto>> { }
