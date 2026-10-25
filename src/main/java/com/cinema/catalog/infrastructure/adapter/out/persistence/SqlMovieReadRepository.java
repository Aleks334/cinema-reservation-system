package com.cinema.catalog.infrastructure.adapter.out.persistence;

import com.cinema.catalog.application.query.dto.MovieDto;
import com.cinema.catalog.application.query.port.MovieReadRepository;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SqlMovieReadRepository implements MovieReadRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlMovieReadRepository.class);
    private final DataSource dataSource;

    @Inject
    public SqlMovieReadRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<MovieDto> findById(String movieId) {
        String sql = """
                      SELECT id, title, director_first_name, director_last_name,
                             description, genre, duration_minutes
                      FROM movies
                      WHERE id = ?;
                      """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, movieId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToMovieDto(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to find movie by id: {}", movieId, e);
            throw new RuntimeException("Failed to read movie from database", e);
        }
    }

    @Override
    public List<MovieDto> findAll() {
        String sql = """
                      SELECT id, title, director_first_name, director_last_name,
                             description, genre, duration_minutes
                      FROM movies;
                      """;

        List<MovieDto> movies = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                movies.add(mapToMovieDto(rs));
            }

            return movies;
        } catch (SQLException e) {
            LOGGER.error("Failed to fetch all movies", e);
            throw new RuntimeException("Failed to read movies from database", e);
        }
    }

    private MovieDto mapToMovieDto(ResultSet rs) throws SQLException {
        String firstName = rs.getString("director_first_name");
        String lastName = rs.getString("director_last_name");
        String directorFullName = (firstName + " " + lastName).trim();

        return new MovieDto(
                rs.getString("id"),
                rs.getString("title"),
                directorFullName,
                rs.getString("description"),
                rs.getString("genre"),
                rs.getInt("duration_minutes")
        );
    }
}