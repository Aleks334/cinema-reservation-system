package com.cinema.application.port.in.query;

import com.cinema.application.port.in.dto.ScreeningDto;
import com.cinema.domain.model.vo.MovieId;
import java.util.List;

public interface GetScreeningsForMovieQuery {
    List<ScreeningDto> getScreeningsForMovie(MovieId movieId);
}
