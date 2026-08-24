package com.cinema.infrastructure.config.modules;

import com.cinema.application.handlers.*;
import com.cinema.application.port.in.command.*;
import com.cinema.application.port.in.query.*;
import com.cinema.application.port.out.CinemaRepository;
import com.cinema.application.port.out.MovieRepository;
import com.cinema.application.port.out.ScreeningRepository;
import com.cinema.infrastructure.adapter.in.web.Controller;
import com.cinema.infrastructure.adapter.in.web.MovieController;
import com.cinema.infrastructure.adapter.in.web.ScreeningController;
import com.cinema.infrastructure.adapter.out.persistence.SqlCinemaRepository;
import com.cinema.infrastructure.adapter.out.persistence.SqlMovieRepository;
import com.cinema.infrastructure.adapter.out.persistence.SqlScreeningRepository;
import com.cinema.infrastructure.bus.InMemoryCommandBus;
import com.cinema.infrastructure.bus.InMemoryQueryBus;
import com.cinema.infrastructure.config.*;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

import java.sql.Connection;
import java.time.Clock;

public class ConfigModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CinemaRepository.class).to(SqlCinemaRepository.class);
        bind(MovieRepository.class).to(SqlMovieRepository.class);
        bind(ScreeningRepository.class).to(SqlScreeningRepository.class);

        MapBinder<Class<? extends Command>, CommandHandler<? extends Command>> commandBinder = MapBinder.newMapBinder(
                binder(),
                new TypeLiteral<>() {},
                new TypeLiteral<>() {}
        );

        commandBinder.addBinding(LockSeatCommand.class).to(LockSeatHandler.class);
        commandBinder.addBinding(ReserveSeatCommand.class).to(ReserveSeatHandler.class);

        MapBinder<Class<? extends Query<?>>, QueryHandler<?, ?>> queryBinder = MapBinder.newMapBinder(
                binder(),
                new TypeLiteral<>() {},
                new TypeLiteral<>() {}
        );
        queryBinder.addBinding(GetMovieQuery.class).to(GetMovieHandler.class);
        queryBinder.addBinding(GetAllMoviesQuery.class).to(GetAllMoviesHandler.class);
        queryBinder.addBinding(GetScreeningQuery.class).to(GetScreeningHandler.class);
        queryBinder.addBinding(GetScreeningsForMovieQuery.class).to(GetScreeningsForMovieHandler.class);

        bind(CommandBus.class).to(InMemoryCommandBus.class);
        bind(QueryBus.class).to(InMemoryQueryBus.class);

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
