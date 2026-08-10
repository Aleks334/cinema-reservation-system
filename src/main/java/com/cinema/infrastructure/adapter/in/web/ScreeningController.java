package com.cinema.infrastructure.adapter.in.web;

import com.cinema.application.port.in.query.GetScreeningQuery;
import com.cinema.application.port.in.command.LockSeatUseCase;
import com.cinema.application.port.in.command.ReserveSeatUseCase;
import com.cinema.application.port.in.dto.ScreeningDto;
import com.cinema.application.port.in.dto.ScreeningSeatDto;
import com.cinema.domain.exception.ScreeningNotFoundException;
import com.cinema.domain.model.vo.ScreeningId;
import com.cinema.domain.model.vo.ScreeningSeatId;
import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiResponse;

public final class ScreeningController {
    private final GetScreeningQuery getScreeningQuery;
    private final LockSeatUseCase lockSeatUseCase;
    private final ReserveSeatUseCase reserveSeatUseCase;

    public ScreeningController(GetScreeningQuery getScreeningQuery,
                               LockSeatUseCase lockSeatUseCase,
                               ReserveSeatUseCase reserveSeatUseCase) {
        this.getScreeningQuery = getScreeningQuery;
        this.lockSeatUseCase = lockSeatUseCase;
        this.reserveSeatUseCase = reserveSeatUseCase;
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

        ScreeningDto screening = getScreeningQuery.getScreening(screeningId)
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
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = ScreeningSeatDto.class)),
                    @OpenApiResponse(status = "404", description = "Not found"),
                    @OpenApiResponse(status = "409", description = "Conflict")
            }
    )
    public void lockSeat(Context ctx) {
        ScreeningId screeningId = ScreeningId.from(ctx.pathParam("screeningId"));
        ScreeningSeatId seatId = ScreeningSeatId.from(ctx.pathParam("seatId"));

        ScreeningSeatDto updatedSeat = lockSeatUseCase.lockSeat(screeningId, seatId);

        ctx.status(200).json(updatedSeat);
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
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = ScreeningSeatDto.class)),
                    @OpenApiResponse(status = "404", description = "Not found"),
                    @OpenApiResponse(status = "409", description = "Conflict")
            }
    )
    public void reserveSeat(Context ctx) {
        ScreeningId screeningId = ScreeningId.from(ctx.pathParam("screeningId"));
        ScreeningSeatId seatId = ScreeningSeatId.from(ctx.pathParam("seatId"));

        ScreeningSeatDto updatedSeat = reserveSeatUseCase.reserveSeat(screeningId, seatId);

        ctx.status(200).json(updatedSeat);
    }
}
