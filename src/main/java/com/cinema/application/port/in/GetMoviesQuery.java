package com.cinema.application.port.in;

import com.cinema.application.port.in.dto.MovieDto;
import java.util.List;

public interface GetMoviesQuery {
    List<MovieDto> getMovies();
}
