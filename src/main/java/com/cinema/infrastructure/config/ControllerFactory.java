package com.cinema.infrastructure.config;

import com.cinema.infrastructure.adapter.in.web.MovieController;
import com.cinema.infrastructure.adapter.in.web.ScreeningController;

public final class ControllerFactory {

    private ControllerFactory() {
    }

    public static Controllers createControllers(ApplicationHandlerFactory.ApplicationHandlers appHandlers) {
        return new Controllers(
                new MovieController(
                        appHandlers.getGetMoviesQueryHandler(),
                        appHandlers.getGetMovieQueryHandler(),
                        appHandlers.getGetScreeningsForMovieQueryHandler()
                ),
                new ScreeningController(
                        appHandlers.getGetScreeningQueryHandler(),
                        appHandlers.getLockSeatUseCaseHandler(),
                        appHandlers.getReserveSeatUseCaseHandler()
                )
        );
    }

    public static final class Controllers {
        private final MovieController movieController;
        private final ScreeningController screeningController;

        private Controllers(MovieController movieController,
                           ScreeningController screeningController) {
            this.movieController = movieController;
            this.screeningController = screeningController;
        }

        public MovieController getMovieController() {
            return movieController;
        }

        public ScreeningController getScreeningController() {
            return screeningController;
        }
    }
}
