package com.cinema.ticketing.application.query;

import com.cinema.ticketing.application.query.dto.ScreeningDto;
import com.cinema.shared.Query;
import com.cinema.ticketing.domain.model.ScreeningId;

import java.util.Optional;

public record GetScreeningQuery(ScreeningId screeningId) implements Query<Optional<ScreeningDto>> { }
