package com.cinema.infrastructure.config.modules;

import com.cinema.application.handlers.*;
import com.cinema.application.port.in.command.*;
import com.cinema.application.port.in.query.*;
import com.cinema.infrastructure.bus.InMemoryCommandBus;
import com.cinema.infrastructure.bus.InMemoryQueryBus;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
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
    }
}
