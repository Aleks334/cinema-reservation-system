package com.cinema;

import com.cinema.domain.exception.LockExpiredException;
import com.cinema.domain.exception.NoSuchMovieFoundException;
import com.cinema.infrastructure.adapter.out.persistence.exception.OptimisticLockException;
import com.cinema.domain.exception.ScreeningNotFoundException;
import com.cinema.domain.exception.SeatAlreadyLockedException;
import com.cinema.domain.exception.SeatNotAvailableException;
import com.cinema.infrastructure.config.AppConfig;
import com.cinema.infrastructure.config.ControllerFactory;
import com.cinema.infrastructure.config.DatabaseConfig;
import com.cinema.infrastructure.config.RepositoryFactory;
import com.cinema.infrastructure.config.ServiceFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.time.Clock;
import java.util.Map;

public final class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private Main() {
    }

    public static void main(String[] args) {
        Clock clock = Clock.systemDefaultZone();
        AppConfig appConfig = new AppConfig();

        Connection connection = DatabaseConfig.getConnection(appConfig);
        RepositoryFactory.Repositories repositories = RepositoryFactory.createRepositories(connection);
        ScreeningService screeningService = ServiceFactory.createScreeningService(
                repositories,
                appConfig.getLockTimeout(),
                clock
        );
        ControllerFactory.Controllers controllers = ControllerFactory.createControllers(screeningService);

        Javalin app = createJavalinApp(appConfig);

        registerRoutes(app, controllers);
        registerExceptionHandlers(app);

        app.start(appConfig.getServerPort());

        LOGGER.info("Cinema Screening System started on port {}", appConfig.getServerPort());
        if (appConfig.isSwaggerEnabled()) {
            LOGGER.info("Swagger UI available at http://localhost:{}/swagger",
                    appConfig.getServerPort());
        }
    }

    private static Javalin createJavalinApp(AppConfig appConfig) {
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

    private static void registerRoutes(Javalin app, ControllerFactory.Controllers controllers) {
        app.get("/api/movies", controllers.getMovieController()::getAllMovies);
        app.get("/api/movies/{id}", controllers.getMovieController()::getMovieById);
        app.get("/api/movies/{movieId}/screenings",
                controllers.getMovieController()::getScreeningsForMovie);

        app.get("/api/screenings/{id}",
                controllers.getScreeningController()::getScreeningById);
        app.post("/api/screenings/{screeningId}/seats/{seatId}/lock",
                controllers.getScreeningController()::lockSeat);
        app.post("/api/screenings/{screeningId}/seats/{seatId}/reserve",
                controllers.getScreeningController()::reserveSeat);
    }

    private static void registerExceptionHandlers(Javalin app) {
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
