package com.cinema;

import com.cinema.domain.exception.LockExpiredException;
import com.cinema.domain.exception.NoSuchMovieFoundException;
import com.cinema.infrastructure.adapter.in.web.MovieController;
import com.cinema.infrastructure.adapter.in.web.ScreeningController;
import com.cinema.infrastructure.adapter.out.persistence.exception.OptimisticLockException;
import com.cinema.domain.exception.ScreeningNotFoundException;
import com.cinema.domain.exception.SeatAlreadyLockedException;
import com.cinema.domain.exception.SeatNotAvailableException;
import com.cinema.infrastructure.config.*;
import com.cinema.infrastructure.config.modules.ConfigModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public final class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private Main() {}

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                Modules.requireAtInjectOnConstructorsModule(),
                new ConfigModule()
        );

        AppConfig appConfig = injector.getInstance(AppConfig.class);

        Application app = injector.getInstance(Application.class);
    }
}
