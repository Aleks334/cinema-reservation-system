package com.cinema.catalog.infrastructure.adapter.in.web;

import com.cinema.catalog.domain.exception.NoSuchMovieFoundException;
import com.cinema.shared.ExceptionHandlerMapper;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CatalogExceptionHandlerMapper implements ExceptionHandlerMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogExceptionHandlerMapper.class);

    @Override
    public void register(Javalin app) {
        app.exception(NoSuchMovieFoundException.class, (e, ctx) -> {
            LOGGER.warn("Movie not found: {}", e.getMessage());
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of("error", "MovieNotFound", "message", e.getMessage()));
        });
    }
}