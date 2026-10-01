package com.cinema.ticketing.infrastructure.adapter.in.web;

import com.cinema.shared.CommandBus;
import com.cinema.shared.Controller;
import com.cinema.shared.QueryBus;
import com.cinema.ticketing.application.command.LockSeatCommand;
import com.cinema.ticketing.application.command.ReserveSeatCommand;
import com.cinema.ticketing.application.query.GetScreeningQuery;
import com.cinema.ticketing.application.query.GetScreeningsForMovieQuery;
import com.cinema.ticketing.application.query.dto.ScreeningDto;
import com.google.inject.Inject;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.openapi.*;

import java.util.List;

public final class ScreeningController implements Controller {
    private final QueryBus queryBus;
    private final CommandBus commandBus;

    @Inject
    public ScreeningController(CommandBus commandBus, QueryBus queryBus) {
        this.queryBus = queryBus;
        this.commandBus = commandBus;
    }

    @Override
    public void register(Javalin app) {
        app.get("/api/screenings/{id}",
                this::getScreeningById);
        app.get("/api/movies/{movieId}/screenings",
                this::getScreeningsForMovie);
        app.post("/api/screenings/{screeningId}/seats/{seatId}/lock",
                this::lockSeat);
        app.post("/api/screenings/{screeningId}/seats/{seatId}/reserve",
                this::reserveSeat);
    }

    @OpenApi(
            path = "/api/screenings/{id}",
            methods = HttpMethod.GET,
            summary = "Get screening by ID",
            description = "Retrieves a screening with all seat information",
            pathParams = {
                    @OpenApiParam(name = "id", description = "Screening UUID", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200",
                            content = @OpenApiContent(from = ScreeningDto.class)),
                    @OpenApiResponse(status = "404", description = "Screening not found")
            }
    )
    private void getScreeningById(Context ctx) {
        String screeningId = ctx.pathParam("id");

        ScreeningDto screening = queryBus.execute(new GetScreeningQuery(screeningId));

        ctx.json(screening);
    }

    @OpenApi(
            path = "/api/movies/{movieId}/screenings",
            methods = HttpMethod.GET,
            summary = "Get screenings for movie",
            description = "Retrieves all screenings for a specific movie",
            pathParams = {
                    @OpenApiParam(name = "movieId", description = "Movie UUID", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200",
                            content = @OpenApiContent(from = ScreeningDto[].class))
            }
    )
    private void getScreeningsForMovie(Context ctx) {
        String movieId = ctx.pathParam("movieId");
        List<ScreeningDto> screenings = queryBus.execute(new GetScreeningsForMovieQuery(movieId));
        ctx.json(screenings);
    }

    @OpenApi(
            path = "/api/screenings/{screeningId}/seats/{seatId}/lock",
            methods = HttpMethod.POST,
            summary = "Lock a seat",
            description = "Locks a seat for temporary reservation",
            pathParams = {
                    @OpenApiParam(name = "screeningId", description = "Screening UUID", required = true),
                    @OpenApiParam(name = "seatId", description = "Seat UUID", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "204", description = "Seat is locked"),
                    @OpenApiResponse(status = "404", description = "Not found"),
                    @OpenApiResponse(status = "409", description = "Conflict")
            }
    )
    private void lockSeat(Context ctx) {
        String screeningId = ctx.pathParam("screeningId");
        String screeningSeatId = ctx.pathParam("seatId");

        commandBus.dispatch(new LockSeatCommand(screeningId, screeningSeatId));
        ctx.status(204);
    }

    @OpenApi(
            path = "/api/screenings/{screeningId}/seats/{seatId}/reserve",
            methods = HttpMethod.POST,
            summary = "Reserve a seat",
            description = "Reserves a previously locked seat",
            pathParams = {
                    @OpenApiParam(name = "screeningId", description = "Screening UUID", required = true),
                    @OpenApiParam(name = "seatId", description = "Seat UUID", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "204", description = "Seat is reserved"),
                    @OpenApiResponse(status = "404", description = "Not found"),
                    @OpenApiResponse(status = "409", description = "Conflict")
            }
    )
    private void reserveSeat(Context ctx) {
        String screeningId = ctx.pathParam("screeningId");
        String screeningSeatId = ctx.pathParam("seatId");

        commandBus.dispatch(new ReserveSeatCommand(screeningId, screeningSeatId));
        ctx.status(204);
    }
}
