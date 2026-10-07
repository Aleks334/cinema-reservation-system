package com.cinema.ticketing.application.query;

import com.cinema.ticketing.application.query.dto.ScreeningDto;
import com.cinema.shared.Query;


public record GetScreeningQuery(String screeningId) implements Query<ScreeningDto> { }
