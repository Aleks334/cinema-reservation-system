package com.cinema.infrastructure.config;

import com.cinema.domain.exception.*;
import com.cinema.infrastructure.adapter.in.web.Controller;
import com.cinema.infrastructure.adapter.out.persistence.exception.OptimisticLockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

@Singleton
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private final AppConfig appConfig;
    private final Set<Controller> controllers;

    @Inject
    public Application(AppConfig appConfig, Set<Controller> controllers) {
        this.appConfig = appConfig;
        this.controllers = controllers;
    }

    public void run() {
        Javalin app = createJavalinApp(appConfig);

        controllers.forEach(controller -> {
            controller.register(app);
        });
        registerExceptionHandlers(app);

        app.start(appConfig.getServerPort());

        LOGGER.info("Cinema Screening System started on port {}", appConfig.getServerPort());
        if (appConfig.isSwaggerEnabled()) {
            LOGGER.info("Swagger UI available at http://localhost:{}/swagger",
                    appConfig.getServerPort());
        }
    }

    private Javalin createJavalinApp(AppConfig appConfig) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(objectMapper, true));

            if (appConfig.isSwaggerEnabled()) {
                config.registerPlugin(new OpenApiPlugin(pluginConfig -> {
                    pluginConfig.withDefinitionConfiguration((version, openApiDefinition) -> {
                        openApiDefinition.withInfo(info -> {
                            info.setTitle("Cinema Screening System API");
                            info.setVersion("1.0.0");
                            info.setDescription("API for managing cinema screenings and seat reservations");
                        });
                    });
                }));
                config.registerPlugin(new SwaggerPlugin());
            }

            if (appConfig.isCorsEnabled()) {
                config.bundledPlugins.enableCors(cors -> {
                    cors.addRule(CorsPluginConfig.CorsRule::anyHost);
                });
            }
        });

        if (appConfig.isCorsEnabled()) {
            app.before(ctx -> {
                ctx.header("Access-Control-Allow-Origin", "*");
                ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            });
        }

        app.before(ctx -> {
            LOGGER.info("{} {}", ctx.method(), ctx.path());
        });

        return app;
    }

    private void registerExceptionHandlers(Javalin app) {
        app.exception(OptimisticLockException.class, (e, ctx) -> {
            LOGGER.warn("Optimistic lock conflict: {}", e.getMessage());
            ctx.status(HttpStatus.CONFLICT);
            ctx.json(Map.of(
                    "error", "Conflict",
                    "message", e.getMessage()
            ));
        });

        app.exception(SeatAlreadyLockedException.class, (e, ctx) -> {
            LOGGER.warn("Seat already locked: {}", e.getMessage());
            ctx.status(HttpStatus.CONFLICT);
            ctx.json(Map.of(
                    "error", "SeatAlreadyLocked",
                    "message", e.getMessage()
            ));
        });

        app.exception(SeatNotAvailableException.class, (e, ctx) -> {
            LOGGER.warn("Seat not available: {}", e.getMessage());
            ctx.status(HttpStatus.CONFLICT);
            ctx.json(Map.of(
                    "error", "SeatNotAvailable",
                    "message", e.getMessage()
            ));
        });

        app.exception(LockExpiredException.class, (e, ctx) -> {
            LOGGER.warn("Lock expired: {}", e.getMessage());
            ctx.status(HttpStatus.CONFLICT);
            ctx.json(Map.of(
                    "error", "LockExpired",
                    "message", e.getMessage()
            ));
        });

        app.exception(ScreeningNotFoundException.class, (e, ctx) -> {
            LOGGER.warn("Screening not found: {}", e.getMessage());
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "error", "ScreeningNotFound",
                    "message", e.getMessage()
            ));
        });

        app.exception(NoSuchMovieFoundException.class, (e, ctx) -> {
            LOGGER.warn("Movie not found: {}", e.getMessage());
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "error", "MovieNotFound",
                    "message", e.getMessage()
            ));
        });

        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            LOGGER.warn("Invalid request: {}", e.getMessage());
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(Map.of(
                    "error", "InvalidRequest",
                    "message", e.getMessage()
            ));
        });

        app.exception(Exception.class, (e, ctx) -> {
            LOGGER.error("Unexpected error", e);
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(Map.of(
                    "error", "InternalServerError",
                    "message", "An unexpected error occurred"
            ));
        });
    }
}
