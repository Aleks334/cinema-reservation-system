package com.cinema.infrastructure.config;

import com.cinema.application.service.ScreeningService;
import com.cinema.infrastructure.adapter.in.web.MovieController;
import com.cinema.infrastructure.adapter.in.web.ScreeningController;

public final class ControllerFactory {

    private ControllerFactory() {
    }

    public static Controllers createControllers(ScreeningService screeningService) {
        return new Controllers(
                new MovieController(
                        screeningService,
                        screeningService,
                        screeningService
                ),
                new ScreeningController(
                        screeningService,
                        screeningService,
                        screeningService
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
