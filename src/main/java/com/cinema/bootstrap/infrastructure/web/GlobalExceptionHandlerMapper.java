package com.cinema.bootstrap.infrastructure.web;

import com.cinema.shared.ExceptionHandlerMapper;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GlobalExceptionHandlerMapper implements ExceptionHandlerMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandlerMapper.class);

    @Override
    public void register(Javalin app) {
        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            LOGGER.warn("Invalid request: {}", e.getMessage());
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(Map.of("error", "InvalidRequest", "message", e.getMessage()));
        });

        app.exception(Exception.class, (e, ctx) -> {
            LOGGER.error("Unexpected error", e);
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(Map.of("error", "InternalServerError", "message", "An unexpected error occurred"));
        });
    }
}