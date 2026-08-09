package com.cinema.application.port.in;

import com.cinema.application.port.in.dto.ScreeningDto;
import com.cinema.domain.model.vo.ScreeningId;
import java.util.Optional;

public interface GetScreeningQuery {
    Optional<ScreeningDto> getScreening(ScreeningId screeningId);
}
