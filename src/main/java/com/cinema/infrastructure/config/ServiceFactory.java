package com.cinema.infrastructure.config;

import java.time.Clock;
import java.time.Duration;

public final class ServiceFactory {

    private ServiceFactory() {
    }

    public static ScreeningService createScreeningService(
            RepositoryFactory.Repositories repositories,
            Duration lockTimeout,
            Clock clock) {
        return new ScreeningService(
                repositories.getMovieRepository(),
                repositories.getScreeningRepository(),
                lockTimeout,
                clock
        );
    }
}
