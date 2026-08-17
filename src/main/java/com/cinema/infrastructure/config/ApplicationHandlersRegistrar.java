package com.cinema.infrastructure.config;

import com.cinema.application.handlers.LockSeatHandler;
import com.cinema.application.handlers.ReserveSeatHandler;
import com.cinema.application.handlers.GetMovieHandler;
import com.cinema.application.handlers.GetAllMoviesHandler;
import com.cinema.application.handlers.GetScreeningHandler;
import com.cinema.application.handlers.GetScreeningsForMovieHandler;
import com.cinema.application.port.in.command.CommandBus;
import com.cinema.application.port.in.command.LockSeatCommand;
import com.cinema.application.port.in.command.ReserveSeatCommand;
import com.cinema.application.port.in.query.*;

import java.time.Clock;
import java.time.Duration;

public final class ApplicationHandlersRegistrar {

    private ApplicationHandlersRegistrar() {}

    public static void register(
            CommandBus commandBus,
            QueryBus queryBus,
            RepositoryFactory.Repositories repositories,
            Duration lockTimeout,
            Clock clock) {

        commandBus.register(LockSeatCommand.class, new LockSeatHandler(
                repositories.getScreeningRepository(),
                lockTimeout,
                clock
        ));
        commandBus.register(ReserveSeatCommand.class, new ReserveSeatHandler(
                repositories.getScreeningRepository(),
                lockTimeout,
                clock
        ));

        queryBus.register(GetMovieQuery.class, new GetMovieHandler(
                repositories.getMovieRepository()
        ));
        queryBus.register(GetAllMoviesQuery.class, new GetAllMoviesHandler(
                repositories.getMovieRepository()
        ));
        queryBus.register(GetScreeningQuery.class, new GetScreeningHandler(
                repositories.getScreeningRepository()
        ));
        queryBus.register(GetScreeningsForMovieQuery.class, new GetScreeningsForMovieHandler(
                repositories.getScreeningRepository()
        ));
    }
}
