package com.cinema.infrastructure.config;

import com.cinema.infrastructure.adapter.out.persistence.SqlCinemaRepository;
import com.cinema.infrastructure.adapter.out.persistence.SqlMovieRepository;
import com.cinema.infrastructure.adapter.out.persistence.SqlScreeningRepository;
import java.sql.Connection;

public final class RepositoryFactory {

    private RepositoryFactory() {}

    public static Repositories create(Connection connection) {
        return new Repositories(
                new SqlMovieRepository(connection),
                new SqlCinemaRepository(connection),
                new SqlScreeningRepository(connection)
        );
    }

    public static final class Repositories {
        private final SqlMovieRepository movieRepository;
        private final SqlCinemaRepository cinemaRepository;
        private final SqlScreeningRepository screeningRepository;

        private Repositories(SqlMovieRepository movieRepository,
                             SqlCinemaRepository cinemaRepository,
                             SqlScreeningRepository screeningRepository) {
            this.movieRepository = movieRepository;
            this.cinemaRepository = cinemaRepository;
            this.screeningRepository = screeningRepository;
        }

        public SqlMovieRepository getMovieRepository() {
            return movieRepository;
        }

        public SqlCinemaRepository getCinemaRepository() {
            return cinemaRepository;
        }

        public SqlScreeningRepository getScreeningRepository() {
            return screeningRepository;
        }
    }
}
