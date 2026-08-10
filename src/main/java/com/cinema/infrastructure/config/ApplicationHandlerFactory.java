package com.cinema.infrastructure.config;

import com.cinema.application.handler.command.LockSeatHandler;
import com.cinema.application.handler.command.ReserveSeatHandler;
import com.cinema.application.handler.query.GetMovieHandler;
import com.cinema.application.handler.query.GetMoviesHandler;
import com.cinema.application.handler.query.GetScreeningHandler;
import com.cinema.application.handler.query.GetScreeningsForMovieHandler;
import com.cinema.application.port.in.command.LockSeatUseCase;
import com.cinema.application.port.in.command.ReserveSeatUseCase;
import com.cinema.application.port.in.query.GetMovieQuery;
import com.cinema.application.port.in.query.GetMoviesQuery;
import com.cinema.application.port.in.query.GetScreeningQuery;
import com.cinema.application.port.in.query.GetScreeningsForMovieQuery;

import java.time.Clock;
import java.time.Duration;

public final class ApplicationHandlerFactory {

    private ApplicationHandlerFactory() {}

    public static ApplicationHandlers createApplicationHandlers(
            RepositoryFactory.Repositories repositories,
            Duration lockTimeout,
            Clock clock) {
        return new ApplicationHandlers(
                new GetMovieHandler(repositories.getMovieRepository()),
                new GetMoviesHandler(repositories.getMovieRepository()),
                new GetScreeningHandler(repositories.getScreeningRepository()),
                new GetScreeningsForMovieHandler(repositories.getScreeningRepository()),
                new LockSeatHandler(repositories.getScreeningRepository(), lockTimeout, clock),
                new ReserveSeatHandler(repositories.getScreeningRepository(), lockTimeout, clock)
                );
    }

    public static final class ApplicationHandlers {
        private final GetMovieQuery getMovieQueryHandler;
        private final GetMoviesQuery getMoviesQueryHandler;
        private final GetScreeningQuery getScreeningQueryHandler;
        private final GetScreeningsForMovieQuery getScreeningsForMovieQueryHandler;
        private final LockSeatUseCase lockSeatUseCaseHandler;
        private final ReserveSeatUseCase reserveSeatUseCaseHandler;

        private ApplicationHandlers(GetMovieQuery getMovieQueryHandler, GetMoviesQuery getMoviesQueryHandler, GetScreeningQuery getScreeningQueryHandler, GetScreeningsForMovieQuery getScreeningsForMovieQueryHandler, LockSeatUseCase lockSeatUseCaseHandler, ReserveSeatUseCase reserveSeatUseCaseHandler) {
            this.getMovieQueryHandler = getMovieQueryHandler;
            this.getMoviesQueryHandler = getMoviesQueryHandler;
            this.getScreeningQueryHandler = getScreeningQueryHandler;
            this.getScreeningsForMovieQueryHandler = getScreeningsForMovieQueryHandler;
            this.lockSeatUseCaseHandler = lockSeatUseCaseHandler;
            this.reserveSeatUseCaseHandler = reserveSeatUseCaseHandler;
        }

        public GetMovieQuery getGetMovieQueryHandler() {
            return getMovieQueryHandler;
        }

        public GetMoviesQuery getGetMoviesQueryHandler() {
            return getMoviesQueryHandler;
        }

        public GetScreeningQuery getGetScreeningQueryHandler() {
            return getScreeningQueryHandler;
        }

        public GetScreeningsForMovieQuery getGetScreeningsForMovieQueryHandler() {
            return getScreeningsForMovieQueryHandler;
        }

        public LockSeatUseCase getLockSeatUseCaseHandler() {
            return lockSeatUseCaseHandler;
        }

        public ReserveSeatUseCase getReserveSeatUseCaseHandler() {
            return reserveSeatUseCaseHandler;
        }
    }
}
