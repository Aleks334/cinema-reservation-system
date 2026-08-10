package com.cinema.application.port.in.query;

import com.cinema.application.port.in.dto.ScreeningDto;
import com.cinema.domain.model.vo.ScreeningId;

import java.util.Optional;

public record GetScreeningQuery(ScreeningId screeningId) implements Query<Optional<ScreeningDto>> { }
