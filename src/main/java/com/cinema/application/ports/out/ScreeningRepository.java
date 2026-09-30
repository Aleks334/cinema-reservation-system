package com.cinema.application.ports.out;

import com.cinema.domain.model.ticketing.Screening;
import com.cinema.domain.model.catalog.MovieId;
import com.cinema.domain.model.ticketing.ScreeningId;

import java.util.List;
import java.util.Optional;

public interface ScreeningRepository {
    Optional<Screening> findById(ScreeningId screeningId);
    List<Screening> findByMovieId(MovieId movieId);
    void save(Screening screening);
}
