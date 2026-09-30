package com.cinema.infrastructure.adapter.in.web;

import com.cinema.application.queries.GetAllMoviesQuery;
import com.cinema.application.queries.GetMovieQuery;
import com.cinema.application.queries.GetScreeningsForMovieQuery;
import com.cinema.infrastructure.bus.QueryBus;
import com.cinema.application.dto.MovieDto;
import com.cinema.application.dto.ScreeningDto;
import com.cinema.domain.exception.NoSuchMovieFoundException;
import com.cinema.domain.model.catalog.MovieId;
import com.google.inject.Inject;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiResponse;
import java.util.List;

public final class MovieController implements Controller {
    private final QueryBus queryBus;

    @Inject
    public MovieController(QueryBus queryBus) {
        this.queryBus = queryBus;
    }

    @Override
    public void register(Javalin app) {
        app.get("/api/movies", this::getAllMovies);
        app.get("/api/movies/{id}", this::getMovieById);
        app.get("/api/movies/{movieId}/screenings",
                this::getScreeningsForMovie);
    }

    @OpenApi(
            path = "/api/movies",
            methods = HttpMethod.GET,
            summary = "Get all movies",
            description = "Retrieves a list of all available movies",
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = MovieDto[].class))
            }
    )
    private void getAllMovies(Context ctx) {
        List<MovieDto> movies = queryBus.execute(new GetAllMoviesQuery());
        ctx.json(movies);
    }

    @OpenApi(
            path = "/api/movies/{id}",
            methods = HttpMethod.GET,
            summary = "Get movie by ID",
            description = "Retrieves a specific movie by its UUID",
            pathParams = {
                    @OpenApiParam(name = "id", description = "Movie UUID", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200", content = @OpenApiContent(from = MovieDto.class)),
                    @OpenApiResponse(status = "404", description = "Movie not found")
            }
    )
    private void getMovieById(Context ctx) {
        MovieId movieId = MovieId.from(ctx.pathParam("id"));

        MovieDto movie = queryBus.execute(new GetMovieQuery(movieId))
                .orElseThrow(() -> new NoSuchMovieFoundException(
                        "Movie with ID " + movieId + " not found"
                ));

        ctx.json(movie);
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
        MovieId movieId = MovieId.from(ctx.pathParam("movieId"));
        List<ScreeningDto> screenings = queryBus.execute(new GetScreeningsForMovieQuery(movieId));
        ctx.json(screenings);
    }
}
