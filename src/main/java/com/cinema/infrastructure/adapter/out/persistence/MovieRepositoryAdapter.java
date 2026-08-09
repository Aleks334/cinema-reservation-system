package com.cinema.infrastructure.adapter.out.persistence;

import com.cinema.application.port.out.LoadMoviePort;
import com.cinema.domain.model.MovieGenre;
import com.cinema.domain.model.vo.Director;
import com.cinema.domain.model.vo.Duration;
import com.cinema.domain.model.Movie;
import com.cinema.domain.model.vo.MovieId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class MovieRepositoryAdapter implements LoadMoviePort {
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepositoryAdapter.class);
    private final Map<UUID, Movie> cache = new ConcurrentHashMap<>();

    public MovieRepositoryAdapter(Connection connection) {
        loadAllMoviesIntoCache(connection);
    }

    @Override
    public Optional<Movie> loadById(MovieId movieId) {
        return Optional.ofNullable(cache.get(movieId.value()));
    }

    @Override
    public List<Movie> loadAll() {
        return new ArrayList<>(cache.values());
    }

    private void loadAllMoviesIntoCache(Connection conn) {
        String sql = "SELECT id, title, director_first_name, director_last_name, "
                + "description, genre, duration_minutes FROM movies";

        try (var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Movie movie = mapToMovie(rs);
                cache.put(movie.getId().value(), movie);
            }

            LOGGER.debug("Loaded {} movies into cache", cache.size());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load movies from database", e);
        }
    }

    private Movie mapToMovie(ResultSet rs) throws SQLException {
        return new Movie(
                MovieId.from(rs.getString("id")),
                rs.getString("title"),
                new Director(
                        rs.getString("director_first_name"),
                        rs.getString("director_last_name")
                ),
                rs.getString("description"),
                MovieGenre.fromDisplayName(rs.getString("genre")),
                Duration.ofMinutes(rs.getInt("duration_minutes"))
        );
    }
}
