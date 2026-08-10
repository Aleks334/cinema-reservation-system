package com.cinema.application.port.in.query;

import com.cinema.application.port.in.dto.ScreeningDto;
import java.util.Optional;

public interface GetScreeningHandler {
    Optional<ScreeningDto> execute(GetScreeningQuery query);
}
