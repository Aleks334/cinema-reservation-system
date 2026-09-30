package com.cinema.ticketing.domain.port;

import com.cinema.ticketing.domain.model.Screening;
import com.cinema.catalog.domain.model.MovieId;
import com.cinema.ticketing.domain.model.ScreeningId;

import java.util.List;
import java.util.Optional;

public interface ScreeningRepository {
    Optional<Screening> findById(ScreeningId screeningId);
    List<Screening> findByMovieId(MovieId movieId);
    void save(Screening screening);
}
