package com.cinema.application.queries;

import com.cinema.application.dto.ScreeningDto;
import com.cinema.application.ports.in.Query;
import com.cinema.domain.model.ticketing.ScreeningId;

import java.util.Optional;

public record GetScreeningQuery(ScreeningId screeningId) implements Query<Optional<ScreeningDto>> { }
