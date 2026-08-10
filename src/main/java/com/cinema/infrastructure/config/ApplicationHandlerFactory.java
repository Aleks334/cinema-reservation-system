package com.cinema.infrastructure.config;

import com.cinema.application.service.LockSeatService;
import com.cinema.application.service.ReserveSeatService;
import com.cinema.application.service.GetMovieService;
import com.cinema.application.service.GetMoviesService;
import com.cinema.application.service.GetScreeningService;
import com.cinema.application.service.GetScreeningsForMovieService;
import com.cinema.application.port.in.command.LockSeatUseCase;
import com.cinema.application.port.in.command.ReserveSeatUseCase;
import com.cinema.application.port.in.query.GetMovieHandler;
import com.cinema.application.port.in.query.GetMoviesHandler;
import com.cinema.application.port.in.query.GetScreeningHandler;
import com.cinema.application.port.in.query.GetScreeningsForMovieHandler;

import java.time.Clock;
import java.time.Duration;

public final class ApplicationHandlerFactory {

    private ApplicationHandlerFactory() {}

    public static ApplicationHandlers createApplicationHandlers(
            RepositoryFactory.Repositories repositories,
            Duration lockTimeout,
            Clock clock) {
        return new ApplicationHandlers(
                new GetMovieService(repositories.getMovieRepository()),
                new GetMoviesService(repositories.getMovieRepository()),
                new GetScreeningService(repositories.getScreeningRepository()),
                new GetScreeningsForMovieService(repositories.getScreeningRepository()),
                new LockSeatService(repositories.getScreeningRepository(), lockTimeout, clock),
                new ReserveSeatService(repositories.getScreeningRepository(), lockTimeout, clock)
                );
    }

    public static final class ApplicationHandlers {
        private final GetMovieHandler getMovieHandler;
        private final GetMoviesHandler getMoviesHandler;
        private final GetScreeningHandler getScreeningHandler;
        private final GetScreeningsForMovieHandler getScreeningsForMovieHandler;
        private final LockSeatUseCase lockSeatUseCaseHandler;
        private final ReserveSeatUseCase reserveSeatUseCaseHandler;

        private ApplicationHandlers(GetMovieHandler getMovieHandler, GetMoviesHandler getMoviesHandler, GetScreeningHandler getScreeningHandler, GetScreeningsForMovieHandler getScreeningsForMovieHandler, LockSeatUseCase lockSeatUseCaseHandler, ReserveSeatUseCase reserveSeatUseCaseHandler) {
            this.getMovieHandler = getMovieHandler;
            this.getMoviesHandler = getMoviesHandler;
            this.getScreeningHandler = getScreeningHandler;
            this.getScreeningsForMovieHandler = getScreeningsForMovieHandler;
            this.lockSeatUseCaseHandler = lockSeatUseCaseHandler;
            this.reserveSeatUseCaseHandler = reserveSeatUseCaseHandler;
        }

        public GetMovieHandler getGetMovieQueryHandler() {
            return getMovieHandler;
        }

        public GetMoviesHandler getGetMoviesQueryHandler() {
            return getMoviesHandler;
        }

        public GetScreeningHandler getGetScreeningQueryHandler() {
            return getScreeningHandler;
        }

        public GetScreeningsForMovieHandler getGetScreeningsForMovieQueryHandler() {
            return getScreeningsForMovieHandler;
        }

        public LockSeatUseCase getLockSeatUseCaseHandler() {
            return lockSeatUseCaseHandler;
        }

        public ReserveSeatUseCase getReserveSeatUseCaseHandler() {
            return reserveSeatUseCaseHandler;
        }
    }
}
