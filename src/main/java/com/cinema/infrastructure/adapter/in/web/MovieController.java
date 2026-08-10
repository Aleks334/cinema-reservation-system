package com.cinema.infrastructure.adapter.in.web;

import com.cinema.application.port.in.query.GetMovieHandler;
import com.cinema.application.port.in.query.GetMoviesHandler;
import com.cinema.application.port.in.query.GetScreeningsForMovieHandler;
import com.cinema.application.port.in.dto.MovieDto;
import com.cinema.application.port.in.dto.ScreeningDto;
import com.cinema.domain.exception.NoSuchMovieFoundException;
import com.cinema.domain.model.vo.MovieId;
import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiResponse;
import java.util.List;

public final class MovieController {
    private final GetMoviesHandler getMoviesHandler;
    private final GetMovieHandler getMovieHandler;
    private final GetScreeningsForMovieHandler getScreeningsForMovieHandler;

    public MovieController(GetMoviesHandler getMoviesHandler,
                           GetMovieHandler getMovieHandler,
                           GetScreeningsForMovieHandler getScreeningsForMovieHandler) {
        this.getMoviesHandler = getMoviesHandler;
        this.getMovieHandler = getMovieHandler;
        this.getScreeningsForMovieHandler = getScreeningsForMovieHandler;
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
    public void getAllMovies(Context ctx) {
        List<MovieDto> movies = getMoviesHandler.execute();
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
    public void getMovieById(Context ctx) {
        MovieId movieId = MovieId.from(ctx.pathParam("id"));

        MovieDto movie = getMovieHandler.execute(movieId)
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
    public void getScreeningsForMovie(Context ctx) {
        MovieId movieId = MovieId.from(ctx.pathParam("movieId"));
        List<ScreeningDto> screenings = getScreeningsForMovieHandler.execute(movieId);
        ctx.json(screenings);
    }
}
