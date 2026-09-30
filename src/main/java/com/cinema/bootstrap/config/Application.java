package com.cinema.bootstrap.config;

import com.cinema.shared.Controller;
import com.cinema.shared.ExceptionHandlerMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@Singleton
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private final AppConfig appConfig;
    private final Set<Controller> controllers;
    private final Set<ExceptionHandlerMapper> exceptionHandlerMappers;

    @Inject
    public Application(AppConfig appConfig, Set<Controller> controllers, Set<ExceptionHandlerMapper> exceptionHandlerMappers) {
        this.appConfig = appConfig;
        this.controllers = controllers;
        this.exceptionHandlerMappers = exceptionHandlerMappers;
    }

    public void run() {
        Javalin app = createJavalinApp(appConfig);

        controllers.forEach(controller -> controller.register(app));
        exceptionHandlerMappers.forEach(mapper -> mapper.register(app));

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
}
