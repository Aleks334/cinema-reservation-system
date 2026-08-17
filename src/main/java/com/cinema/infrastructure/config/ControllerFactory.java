package com.cinema.infrastructure.config;

import com.cinema.application.port.in.command.CommandBus;
import com.cinema.application.port.in.query.QueryBus;
import com.cinema.infrastructure.adapter.in.web.MovieController;
import com.cinema.infrastructure.adapter.in.web.ScreeningController;

public final class ControllerFactory {

    private ControllerFactory() {
    }

    public static Controllers create(CommandBus commandBus, QueryBus queryBus) {
        return new Controllers(
                new MovieController(queryBus),
                new ScreeningController(commandBus, queryBus)
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
