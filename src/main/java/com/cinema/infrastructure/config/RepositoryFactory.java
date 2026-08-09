package com.cinema.infrastructure.config;

import com.cinema.infrastructure.adapter.out.persistence.CinemaRepositoryAdapter;
import com.cinema.infrastructure.adapter.out.persistence.MovieRepositoryAdapter;
import com.cinema.infrastructure.adapter.out.persistence.ScreeningRepositoryAdapter;
import java.sql.Connection;

public final class RepositoryFactory {

    private RepositoryFactory() {
    }

    public static Repositories createRepositories(Connection connection) {
        return new Repositories(
                new MovieRepositoryAdapter(connection),
                new CinemaRepositoryAdapter(connection),
                new ScreeningRepositoryAdapter(connection)
        );
    }

    public static final class Repositories {
        private final MovieRepositoryAdapter movieRepository;
        private final CinemaRepositoryAdapter cinemaRepository;
        private final ScreeningRepositoryAdapter screeningRepository;

        private Repositories(MovieRepositoryAdapter movieRepository,
                            CinemaRepositoryAdapter cinemaRepository,
                            ScreeningRepositoryAdapter screeningRepository) {
            this.movieRepository = movieRepository;
            this.cinemaRepository = cinemaRepository;
            this.screeningRepository = screeningRepository;
        }

        public MovieRepositoryAdapter getMovieRepository() {
            return movieRepository;
        }

        public CinemaRepositoryAdapter getCinemaRepository() {
            return cinemaRepository;
        }

        public ScreeningRepositoryAdapter getScreeningRepository() {
            return screeningRepository;
        }
    }
}
