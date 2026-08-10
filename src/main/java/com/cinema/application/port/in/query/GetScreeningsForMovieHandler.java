package com.cinema.application.port.in.query;

import com.cinema.application.port.in.dto.ScreeningDto;
import java.util.List;

public interface GetScreeningsForMovieHandler {
    List<ScreeningDto> execute(GetScreeningsForMovieQuery query);
}
