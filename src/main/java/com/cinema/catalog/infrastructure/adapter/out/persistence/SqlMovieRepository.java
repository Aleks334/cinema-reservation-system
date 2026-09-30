package com.cinema.catalog.infrastructure.adapter.out.persistence;

import com.cinema.catalog.domain.port.MovieRepository;
import com.cinema.catalog.domain.model.Movie;
import com.cinema.catalog.domain.model.MovieId;
import com.cinema.catalog.infrastructure.adapter.out.persistence.mapping.MovieMapper;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SqlMovieRepository implements MovieRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlMovieRepository.class);
    private final Map<UUID, Movie> cache = new ConcurrentHashMap<>();

    @Inject
    public SqlMovieRepository(Connection connection) {
        loadAllMoviesIntoCache(connection);
    }

    @Override
    public Optional<Movie> findById(MovieId movieId) {
        return Optional.ofNullable(cache.get(movieId.value()));
    }

    @Override
    public List<Movie> getAll() {
        return new ArrayList<>(cache.values());
    }

    private void loadAllMoviesIntoCache(Connection conn) {
        String sql = """
                      SELECT id, title, director_first_name, director_last_name,
                             description, genre, duration_minutes
                      FROM movies;
                      """;

        try (var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Movie movie = MovieMapper.toMovie(rs);
                cache.put(movie.getId().value(), movie);
            }

            LOGGER.debug("Loaded {} movies into cache", cache.size());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load movies from database", e);
        }
    }
}
