package com.cinema.application.service;

import com.cinema.application.mapping.ScreeningMapper;
import com.cinema.application.port.in.query.GetScreeningsForMovieHandler;
import com.cinema.application.port.in.dto.ScreeningDto;
import com.cinema.application.port.in.query.GetScreeningsForMovieQuery;
import com.cinema.application.port.out.ScreeningRepository;

import java.util.List;
import java.util.Objects;

public class GetScreeningsForMovieService implements GetScreeningsForMovieHandler {

    private final ScreeningRepository repository;

    public GetScreeningsForMovieService(ScreeningRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ScreeningDto> execute(GetScreeningsForMovieQuery query) {
        Objects.requireNonNull(query.movieId());
        return repository.findByMovieId(query.movieId()).stream()
                .map(ScreeningMapper::toDto)
                .toList();
    }
}
