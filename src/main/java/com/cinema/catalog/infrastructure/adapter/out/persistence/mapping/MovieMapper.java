package com.cinema.catalog.infrastructure.adapter.out.persistence.mapping;

import com.cinema.catalog.domain.model.Movie;
import com.cinema.catalog.domain.model.MovieGenre;
import com.cinema.catalog.domain.model.Director;
import com.cinema.catalog.domain.model.MovieDuration;
import com.cinema.catalog.domain.model.MovieId;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class MovieMapper {
    private MovieMapper() {}

    public static Movie toMovie(ResultSet rs) throws SQLException {
        return new Movie(
                MovieId.from(rs.getString("id")),
                rs.getString("title"),
                new Director(
                        rs.getString("director_first_name"),
                        rs.getString("director_last_name")
                ),
                rs.getString("description"),
                MovieGenre.fromDisplayName(rs.getString("genre")),
                MovieDuration.ofMinutes(rs.getInt("duration_minutes"))
        );
    }
}
