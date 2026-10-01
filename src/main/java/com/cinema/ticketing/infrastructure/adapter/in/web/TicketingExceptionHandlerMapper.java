package com.cinema.ticketing.infrastructure.adapter.in.web;

import com.cinema.shared.ExceptionHandlerMapper;
import com.cinema.ticketing.domain.exception.*;
import com.cinema.ticketing.infrastructure.adapter.out.persistence.exception.OptimisticLockException;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TicketingExceptionHandlerMapper implements ExceptionHandlerMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketingExceptionHandlerMapper.class);

    @Override
    public void register(Javalin app) {
        app.exception(SeatAlreadyLockedException.class, (e, ctx) -> {
            LOGGER.warn("Seat already locked: {}", e.getMessage());
            ctx.status(HttpStatus.CONFLICT);
            ctx.json(Map.of("error", "SeatAlreadyLocked", "message", e.getMessage()));
        });

        app.exception(SeatNotAvailableException.class, (e, ctx) -> {
            LOGGER.warn("Seat not available: {}", e.getMessage());
            ctx.status(HttpStatus.CONFLICT);
            ctx.json(Map.of("error", "SeatNotAvailable", "message", e.getMessage()));
        });

        app.exception(LockExpiredException.class, (e, ctx) -> {
            LOGGER.warn("Lock expired: {}", e.getMessage());
            ctx.status(HttpStatus.CONFLICT);
            ctx.json(Map.of("error", "LockExpired", "message", e.getMessage()));
        });

        app.exception(ScreeningNotFoundException.class, (e, ctx) -> {
            LOGGER.warn("Screening not found: {}", e.getMessage());
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of("error", "ScreeningNotFound", "message", e.getMessage()));
        });

        app.exception(OptimisticLockException.class, (e, ctx) -> {
            LOGGER.warn("Optimistic lock conflict: {}", e.getMessage());
            ctx.status(HttpStatus.CONFLICT);
            ctx.json(Map.of(
                    "error", "Conflict",
                    "message", e.getMessage()
            ));
        });
    }
}