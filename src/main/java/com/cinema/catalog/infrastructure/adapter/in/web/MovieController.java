package com.cinema.catalog.infrastructure.adapter.in.web;

import com.cinema.catalog.application.query.GetAllMoviesQuery;
import com.cinema.catalog.application.query.GetMovieQuery;
import com.cinema.shared.Controller;
import com.cinema.shared.QueryBus;
import com.cinema.catalog.application.query.dto.MovieDto;
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
        String movieId = ctx.pathParam("id");

        MovieDto movie = queryBus.execute(new GetMovieQuery(movieId));

        ctx.json(movie);
    }


}
