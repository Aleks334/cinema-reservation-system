package com.cinema.application.port.in.query;

import com.cinema.application.port.in.dto.ScreeningDto;
import com.cinema.domain.model.vo.ScreeningId;
import java.util.Optional;

public interface GetScreeningHandler {
    Optional<ScreeningDto> execute(ScreeningId screeningId);
}
