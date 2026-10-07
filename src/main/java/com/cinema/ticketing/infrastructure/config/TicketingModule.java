package com.cinema.ticketing.infrastructure.config;

import com.cinema.shared.*;
import com.cinema.ticketing.application.command.LockSeatCommand;
import com.cinema.ticketing.application.command.LockSeatHandler;
import com.cinema.ticketing.application.command.ReserveSeatCommand;
import com.cinema.ticketing.application.command.ReserveSeatHandler;
import com.cinema.ticketing.application.query.GetScreeningHandler;
import com.cinema.ticketing.application.query.GetScreeningQuery;
import com.cinema.ticketing.application.query.GetScreeningsForMovieHandler;
import com.cinema.ticketing.application.query.GetScreeningsForMovieQuery;
import com.cinema.ticketing.domain.port.ScreeningRepository;
import com.cinema.ticketing.infrastructure.adapter.in.web.ScreeningController;
import com.cinema.ticketing.infrastructure.adapter.in.web.TicketingExceptionHandlerMapper;
import com.cinema.ticketing.infrastructure.adapter.out.persistence.SqlScreeningRepository;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

public class TicketingModule extends AbstractModule {

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

        queryBinder.addBinding(GetScreeningQuery.class).to(GetScreeningHandler.class);
        queryBinder.addBinding(GetScreeningsForMovieQuery.class).to(GetScreeningsForMovieHandler.class);

        Multibinder<Controller> controllerBinder = Multibinder.newSetBinder(binder(), Controller.class);
        controllerBinder.addBinding().to(ScreeningController.class);

        bind(ScreeningRepository.class).to(SqlScreeningRepository.class);

        Multibinder<ExceptionHandlerMapper> exceptionBinder = Multibinder.newSetBinder(binder(), ExceptionHandlerMapper.class);
        exceptionBinder.addBinding().toInstance(new TicketingExceptionHandlerMapper());
    }
}
