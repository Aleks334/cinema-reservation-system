package com.cinema.infrastructure.config.modules;

import com.cinema.application.port.out.CinemaRepository;
import com.cinema.application.port.out.MovieRepository;
import com.cinema.application.port.out.ScreeningRepository;
import com.cinema.infrastructure.adapter.in.web.Controller;
import com.cinema.infrastructure.adapter.in.web.MovieController;
import com.cinema.infrastructure.adapter.in.web.ScreeningController;
import com.cinema.infrastructure.adapter.out.persistence.SqlCinemaRepository;
import com.cinema.infrastructure.adapter.out.persistence.SqlMovieRepository;
import com.cinema.infrastructure.adapter.out.persistence.SqlScreeningRepository;
import com.cinema.infrastructure.config.AppConfig;
import com.cinema.infrastructure.config.DatabaseConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;

import java.sql.Connection;
import java.time.Clock;

public class InfrastructureModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CinemaRepository.class).to(SqlCinemaRepository.class);
        bind(MovieRepository.class).to(SqlMovieRepository.class);
        bind(ScreeningRepository.class).to(SqlScreeningRepository.class);

        Multibinder<Controller> controllerBinder = Multibinder.newSetBinder(binder(), Controller.class);
        controllerBinder.addBinding().to(MovieController.class);
        controllerBinder.addBinding().to(ScreeningController.class);
    }

    @Provides
    private static Clock provideClock() {
        return Clock.systemUTC();
    }

    @Provides
    private static AppConfig provideAppConfig() {
        return new AppConfig();
    }

    @Provides
    private static DatabaseConfig provideDatabaseConfig(AppConfig appConfig) {
        return new DatabaseConfig(appConfig);
    }

    @Provides
    private static Connection provideConnection(DatabaseConfig dbConfig) {
        return dbConfig.getConnection();
    }
}
