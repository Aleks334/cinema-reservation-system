package com.cinema.application.handler.query;

import com.cinema.application.mapping.ScreeningMapper;
import com.cinema.application.port.in.query.GetScreeningsForMovieQuery;
import com.cinema.application.port.in.dto.ScreeningDto;
import com.cinema.application.port.out.ScreeningRepository;
import com.cinema.domain.model.vo.MovieId;

import java.util.List;
import java.util.Objects;

public class GetScreeningsForMovieHandler implements GetScreeningsForMovieQuery {

    private final ScreeningRepository repository;

    public GetScreeningsForMovieHandler(ScreeningRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ScreeningDto> getScreeningsForMovie(MovieId movieId) {
        Objects.requireNonNull(movieId);
        return repository.findByMovieId(movieId).stream()
                .map(ScreeningMapper::toDto)
                .toList();
    }
}
