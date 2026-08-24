package com.cinema.infrastructure.adapter.in.web;

import com.cinema.application.port.in.command.CommandBus;
import com.cinema.application.port.in.command.LockSeatCommand;
import com.cinema.application.port.in.command.ReserveSeatCommand;
import com.cinema.application.dto.ScreeningDto;
import com.cinema.application.port.in.query.GetScreeningQuery;
import com.cinema.application.port.in.query.QueryBus;
import com.cinema.domain.exception.ScreeningNotFoundException;
import com.cinema.domain.model.ticketing.ScreeningId;
import com.cinema.domain.model.ticketing.ScreeningSeatId;
import com.google.inject.Inject;
import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiResponse;

public final class ScreeningController {
    private final QueryBus queryBus;
    private final CommandBus commandBus;

    @Inject
    public ScreeningController(CommandBus commandBus, QueryBus queryBus) {
        this.queryBus = queryBus;
        this.commandBus = commandBus;
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
    public void getScreeningById(Context ctx) {
        ScreeningId screeningId = ScreeningId.from(ctx.pathParam("id"));

        ScreeningDto screening = queryBus.execute(new GetScreeningQuery(screeningId))
                .orElseThrow(() -> new ScreeningNotFoundException(
                        "Screening with ID " + screeningId + " not found"
                ));

        ctx.json(screening);
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
    public void lockSeat(Context ctx) {
        ScreeningId screeningId = ScreeningId.from(ctx.pathParam("screeningId"));
        ScreeningSeatId screeningSeatId = ScreeningSeatId.from(ctx.pathParam("seatId"));

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
    public void reserveSeat(Context ctx) {
        ScreeningId screeningId = ScreeningId.from(ctx.pathParam("screeningId"));
        ScreeningSeatId screeningSeatId = ScreeningSeatId.from(ctx.pathParam("seatId"));

        commandBus.dispatch(new ReserveSeatCommand(screeningId, screeningSeatId));
        ctx.status(204);
    }
}
