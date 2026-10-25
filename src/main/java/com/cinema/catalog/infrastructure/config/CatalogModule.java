package com.cinema.catalog.infrastructure.config;

import com.cinema.catalog.application.query.*;
import com.cinema.catalog.application.query.port.MovieReadRepository;
import com.cinema.catalog.infrastructure.adapter.in.web.CatalogExceptionHandlerMapper;
import com.cinema.catalog.infrastructure.adapter.in.web.MovieController;
import com.cinema.catalog.infrastructure.adapter.out.persistence.SqlMovieReadRepository;
import com.cinema.shared.Controller;
import com.cinema.shared.ExceptionHandlerMapper;
import com.cinema.shared.Query;
import com.cinema.shared.QueryHandler;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

public class CatalogModule extends AbstractModule {
    @Override
    protected void configure() {
        MapBinder<Class<? extends Query<?>>, QueryHandler<?, ?>> queryBinder = MapBinder.newMapBinder(
                binder(),
                new TypeLiteral<>() {},
                new TypeLiteral<>() {}
        );

        queryBinder.addBinding(GetMovieQuery.class).to(GetMovieHandler.class);
        queryBinder.addBinding(GetAllMoviesQuery.class).to(GetAllMoviesHandler.class);

        bind(MovieReadRepository.class).to(SqlMovieReadRepository.class);

        Multibinder<Controller> controllerBinder = Multibinder.newSetBinder(binder(), Controller.class);
        controllerBinder.addBinding().to(MovieController.class);

        Multibinder<ExceptionHandlerMapper> exceptionBinder = Multibinder.newSetBinder(binder(), ExceptionHandlerMapper.class);
        exceptionBinder.addBinding().toInstance(new CatalogExceptionHandlerMapper());
    }
}