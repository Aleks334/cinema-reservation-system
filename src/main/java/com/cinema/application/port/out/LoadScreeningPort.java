package com.cinema.application.port.out;

import com.cinema.domain.model.vo.MovieId;
import com.cinema.domain.model.Screening;
import com.cinema.domain.model.vo.ScreeningId;
import java.util.List;
import java.util.Optional;

public interface LoadScreeningPort {
    Optional<Screening> loadById(ScreeningId screeningId);

    List<Screening> loadByMovieId(MovieId movieId);
}
