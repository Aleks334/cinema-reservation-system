package com.cinema.application.port.out;

import com.cinema.domain.model.Screening;
import com.cinema.domain.model.vo.MovieId;
import com.cinema.domain.model.vo.ScreeningId;

import java.util.List;
import java.util.Optional;

public interface ScreeningRepository {
    Optional<Screening> findById(ScreeningId screeningId);
    List<Screening> findByMovieId(MovieId movieId);
    void save(Screening screening);
}
